<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px" v-loading="loading" element-loading-text="识别中...">
      <el-form-item label="书名">
        <el-input v-model="form.title" :class="{ 'error': isTitleEmpty }" @input="validateTitle"/>
        <div v-if="isTitleEmpty" class="error-message">书名不能为空!!!</div>
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="form.author" :class="{ 'error': isAuthorEmpty }" @input="validateAuthor"/>
        <div v-if="isAuthorEmpty" class="error-message">作者不能为空!!!</div>
      </el-form-item>
      <el-form-item label="ISBN">
        <el-input v-model="form.isbn" :class="{ 'error': isIsbnEmpty }" @input="validateIsbn"/>
        <div v-if="isIsbnEmpty" class="error-message">ISBN 不能为空!!!</div>
      </el-form-item>
      <el-form-item label="简介">
        <el-input v-model="form.introduction" type="textarea" />
      </el-form-item>

      <el-form-item label="" style="display: flex; align-items: center;">
        <!-- 上传图片->文字识别 -->
        <input type="file" ref="fileInput" @change="handleFileUpload" style="display: none;"/>
        <img 
          src="@/icons/svg/scan.png" 
          height="40" 
          width="40" 
          alt="Upload" 
          @click="triggerFileInput" 
          style="cursor: pointer; margin-right: 40px;"
          class="img_buttom"
        /> 

        <el-button type="primary" @click="onSubmit" :loading="submitLoading" :disabled="!formIsValid" style="margin-left: 16px;">完成</el-button>
        <el-button @click="onCancel" style="margin-left: 8px;">取消</el-button>

      </el-form-item>
    </el-form>
  </div>
</template>


<script>
import { ocr, addBook } from '@/api/book'

export default {
  data() {
    return {
      form: {
        title: '',
        author: '',
        isbn: '',
        introduction: ''
      },
      isTitleEmpty: false, // 书名是否为空
      isAuthorEmpty: false, // 作者是否为空
      isIsbnEmpty: false, // isbn是否为空
      submitLoading: false, // 提交状态
      loading: false // 加载
    }
  },
  computed: {
    formIsValid() {
      return this.form.title.trim() !== '' &&
             this.form.author.trim() !== '' &&
             this.form.isbn.trim() !== '';
    }
  },
  methods: {
    // 打开文件
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    // 上传图片到后端进行文字识别
    handleFileUpload(event) {
      // 加载
      this.loading = true;

      const file = event.target.files[0];
      if (!file) {
        // 停止加载
        this.loading = false;
        return;
      }
      // 创建一个FormData对象来上传文件
      const formData = new FormData();
      formData.append('file', file);

      // 发送请求到后端进行文字识别
      ocr(formData).then(response => {
        // 重置文件输入
        event.target.value = '';
        // 便于检查
        console.log('识别结果:', response.data);

        // { title: 'Book Name', author: 'Author Name', isbn: 'ISBN Number', introduction: 'Book Introduction'}
        const bookInfo = response.data;

        // 填充表单
        this.fillBookInfo(bookInfo);
      }).catch(error => {
        // 重置文件输入
        event.target.value = '';

        // 停止加载
        this.loading = false;

        console.error('识别失败:', error);
        this.$message.error('图书信息识别失败,请手动填写')
      });
    },
    // 填充表单
    fillBookInfo(bookInfo) {
      // 加载完毕
      this.loading = false;

      // 将图书信息填充到表单中
      this.form.title = bookInfo.title;
      this.form.author = bookInfo.author;
      this.form.isbn = bookInfo.isbn;
      this.form.introduction = bookInfo.introduction;

      // 提示用户进行检查、修改
      this.$message({
        duration: 5000,
        message: "识别成功!请核对，如有错误请手动修改!",
        type: 'success'
      });
    },
    // 书名是否有效
    validateTitle() {
      this.isTitleEmpty = this.form.title.trim() === '';
    },
    // 作者是否有效
    validateAuthor() {
      this.isAuthorEmpty = this.form.author.trim() === '';
    },
    // ISBN号是否有效
    validateIsbn() {
      this.isIsbnEmpty = this.form.isbn.trim() === '';
    },
    // 提交
    onSubmit() {
      // 先判断表单是否合理
      if (!this.formIsValid) {
        this.$message.error('请确保书名、作者和 ISBN 号不为空！');
        return;
      }

      // 获得图书数据
      const bookData = {
        title: this.form.title.replace(/《|》|「|」/g, ''), // 去除书名号
        author: this.form.author,
        isbn: this.form.isbn.replace(/-/g, ''), // 去除ISBN中的横杠
        introduction: this.form.introduction,
      };

      // 禁用按钮防止重复提交
      this.submitLoading = true;

      // 发送请求到后端录入图书
      addBook(bookData).then(response => {
        this.$message.success('图书录入成功!');
        // 清空表单
        this.form = {
          title: '',
          author: '',
          isbn: '',
          introduction: ''
        };
        this.isTitleEmpty = false;
        this.isAuthorEmpty = false;
        this.isIsbnEmpty = false;
      }).catch(error =>{
        // 停止加载
        this.loading = false;

        console.error('录入失败:', error);
      }).finally(() => {
        // 无论成功还是失败,都解除按钮禁用
        this.submitLoading = false;
      })
    },
    // 取消
    onCancel() {
      this.$confirm('确定要放弃当前编辑吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.form = this.$options.data().form;
        // 重置文件输入
        this.$refs.fileInput.value = '';

        this.$message.info({
          message: '已取消编辑',
          type: 'warning'
        });
      });
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
.img_buttom{
  transform: translateY(15px);
}
.error-message {
  color: red;
  font-size: 12px;
  margin-top: 5px;
}
</style>