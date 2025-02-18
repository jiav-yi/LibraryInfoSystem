<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card id="search">
      <el-input v-model="searchModel.title" placeholder="书名" clearable></el-input>
      <el-input v-model="searchModel.isbn" placeholder="ISBN" clearable></el-input>
      <el-input v-model="searchModel.author" placeholder="作者" clearable></el-input>
      <el-button @click="getBookList" type="primary" icon="el-icon-search">查询</el-button>
    </el-card>

    <!-- 结果列表 -->
    <el-card>
      <el-table :data="bookList" stripe style="width: 100%">
        <el-table-column label="#" width="80">
          <template slot-scope="scope">
            <!-- (pageNo - 1) * pageSize + index + 1 -->
            {{ (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="title" label="书名" width="180">
        </el-table-column>
        <el-table-column prop="author" label="作者" width="180">
        </el-table-column>
        <el-table-column prop="isbn" label="ISBN" width="180">
        </el-table-column>
        <el-table-column prop="introduction" label="简介" width="370">
          <template slot-scope="scope">
            <div :title="scope.row.introduction">
              {{ scope.row.introduction.length > 70 ? scope.row.introduction.slice(0, 70) + '......' : scope.row.introduction }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-link @click="openEditUI(scope.row.isbn)" icon="el-icon-edit" style="margin-right: 10px">编辑</el-link>
            <el-link @click="goToDetail(scope.row)">详情<i class="el-icon-view el-icon--right"></i> </el-link>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页组件 -->
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="searchModel.pageNo" :page-sizes="[5, 10, 20, 50]" :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="total">
    </el-pagination>

    <!-- 图书信息编辑对话框 -->
    <el-dialog @close="clearForm" :title="title" :visible.sync="dialogFormVisible">
      <el-form :model="bookForm" ref="bookFormRef" :rules="rules">
        <el-form-item label="书名" prop="title" :label-width="formLabelWidth">
          <el-input v-model="bookForm.title" autocomplete="off" class="full-width-input"></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="author" :label-width="formLabelWidth">
          <el-input v-model="bookForm.author" autocomplete="off" class="full-width-input"></el-input>
        </el-form-item>
        <el-form-item label="ISBN" prop="isbn" :label-width="formLabelWidth">
          <el-input v-model="bookForm.isbn" autocomplete="off" class="full-width-input"></el-input>
        </el-form-item>
        <el-form-item label="简介" :label-width="formLabelWidth">
          <el-input v-model="bookForm.introduction" autocomplete="off" type="textarea" class="full-width-input"></el-input>
        </el-form-item>
        <el-form-item label="评论" :label-width="formLabelWidth">
          <el-input v-model="bookForm.comments" autocomplete="off" type="textarea" class="full-width-input"></el-input>
        </el-form-item>
        <el-form-item label="库存" :label-width="formLabelWidth">
          <el-input-number v-model="bookForm.stock" :min="0" label="库存"></el-input-number>
        </el-form-item>
        <el-form-item label="销量" :label-width="formLabelWidth">
          <el-input-number v-model="bookForm.sale" :min="0" label="销量"></el-input-number>
        </el-form-item>
        <el-form-item label="封面" :label-width="formLabelWidth">
          <el-image :src="bookForm.image"></el-image>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveBook">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import bookApi from '@/api/book'

export default {
  data() {
    return {
      formLabelWidth: '130px',
      bookForm: {},
      title: "",
      dialogFormVisible: false,
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10
      },
      bookList: [],
      rules: {
        title: [
          { required: true, message: '请输入书名', trigger: 'blur' }
        ],
        author: [
          { required: true, message: '请输入作者', trigger: 'blur' }
        ],
        isbn: [
          { required: true, message: '请输入ISBN', trigger: 'blur' },
          { min: 13, max: 13, message: '长度应为13位', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 分页相关
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize;
      this.getBookList();
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo;
      this.getBookList();
    },
    // 查询图书
    getBookList() {
      bookApi.getBookList(this.searchModel).then(response => {
        this.bookList = response.data.rows;
        this.total = response.data.total;
      });
    },
    // 对话框相关
    openEditUI(isbn) {
      this.title = "编辑图书";
      // 根据isbn查询图书数据
      bookApi.getBookByIsbn(isbn).then(response => {
        this.bookForm = response.data;
      });
      this.dialogFormVisible = true;
    },
    clearForm() {
      this.bookForm = {};
      // 移除校验结果
      this.$refs.bookFormRef.clearValidate();
    },
    saveBook() {
      // 触发表单验证
      this.$refs.bookFormRef.validate((valid) => {
        if (valid) {
          // 提交请求给后台
          bookApi.updateBook(this.bookForm).then(response => {
            // 成功提示
            this.$message({
              message: response.message,
              type: 'success'
            });
            // 关闭对话框
            this.dialogFormVisible = false;
            // 刷新表格
            this.getBookList();
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    // 跳转到详情页
    goToDetail(book) {
      this.$router.push({ path: '/sys/detail', query: { book: JSON.stringify(book) } });
    }
  },
  // 钩子函数
  created() {
    this.getBookList();
  }
}
</script>

<style>
/* 搜索框 */
#search .el-input {
  width: 200px;
  margin-right: 10px;
}
/* 对话框 */
.full-width-input {
  width: 80%;
}
</style>