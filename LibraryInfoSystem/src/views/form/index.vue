<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="书名">
        <el-input v-model="form.title" />
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="form.author" />
      </el-form-item>
      <el-form-item label="ISBN">
        <el-input v-model="form.isbn" />
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

        <el-button type="primary" @click="onSubmit" style="margin-left: 16px;">完成</el-button>
        <el-button @click="onCancel" style="margin-left: 8px;">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>


      <!-- <el-form-item label="Activity zone">
        <el-select v-model="form.region" placeholder="please select your zone">
          <el-option label="Zone one" value="shanghai" />
          <el-option label="Zone two" value="beijing" />
        </el-select>
      </el-form-item>
      <el-form-item label="Activity time">
        <el-col :span="11">
          <el-date-picker v-model="form.date1" type="date" placeholder="Pick a date" style="width: 100%;" />
        </el-col>
        <el-col :span="2" class="line">-</el-col>
        <el-col :span="11">
          <el-time-picker v-model="form.date2" type="fixed-time" placeholder="Pick a time" style="width: 100%;" />
        </el-col>
      </el-form-item>
      <el-form-item label="Instant delivery">
        <el-switch v-model="form.delivery" />
      </el-form-item>
      <el-form-item label="Activity type">
        <el-checkbox-group v-model="form.type">
          <el-checkbox label="Online activities" name="type" />
          <el-checkbox label="Promotion activities" name="type" />
          <el-checkbox label="Offline activities" name="type" />
          <el-checkbox label="Simple brand exposure" name="type" />
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="Resources">
        <el-radio-group v-model="form.resource">
          <el-radio label="Sponsor" />
          <el-radio label="Venue" />
        </el-radio-group>
      </el-form-item> -->
      

<script>
export default {
  data() {
    return {
      form: {
        title: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      }
    }
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (!file) return;

      // 创建一个FormData对象来上传文件
      const formData = new FormData();
      formData.append('file', file);

      // 发送请求到后端进行文字识别
      axios.post('/api/ocr', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then(response => {
        // { title: 'Book Name', author: 'Author Name', isbn: 'ISBN Number', introduction: 'Book Introduction'}
        const bookInfo = response.data;

        this.fillBookInfo(bookInfo);
      }).catch(error => {
        console.error('Error uploading file:', error);
      });


      // // test code 
      // const bookInfo = { 
      //   title: '西游记', 
      //   author: '吴承恩', 
      //   isbn: '9959663677', 
      //   introduction: '讲述了唐僧师徒西游的故事'
      // };
      // this.fillBookInfo(bookInfo);
    },
    fillBookInfo(bookInfo) {
      // 将图书信息填充到表单中
      this.form.title = bookInfo.title;
      this.form.author = bookInfo.author;
      this.form.isbn = bookInfo.isbn;
      this.form.introduction = bookInfo.introduction;
    },

    onSubmit() {
      this.$message('submit!')
    },
    onCancel() {
      this.$message({
        message: 'cancel!',
        type: 'warning'
      })
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
</style>