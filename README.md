# 基于前后端分离和文字识别的图书信息系统开发

​																																				**Designed by <u>郑嘉威</u>**

​																																				**Begin Time <u>2025.1.20</u>**

------

## 前端

### 1.node环境

官网：https://nodejs.org

![](https://pic1.imgdb.cn/item/678e0b0ad0e0a243d4f5dfe9.png)

### 2.vue-admin-template

基于该脚手架进行开发

https://panjiachen.github.io/vue-element-admin-site/zh/guide/

### 3.项目初始化

#### 1)文件目录解析

```sh
├── build                      # 构建相关
├── mock                       # 项目mock 模拟数据
├── plop-templates             # 基本模板
├── public                     # 静态资源
│   │── favicon.ico            # favicon图标
│   └── index.html             # html模板
├── src                        # 源代码
│   ├── api                    # 所有请求
│   ├── assets                 # 主题 字体等静态资源
│   ├── components             # 全局公用组件
│   ├── directive              # 全局指令
│   ├── filters                # 全局 filter
│   ├── icons                  # 项目所有 svg icons
│   ├── lang                   # 国际化 language
│   ├── layout                 # 全局 layout
│   ├── router                 # 路由
│   ├── store                  # 全局 store管理
│   ├── styles                 # 全局样式
│   ├── utils                  # 全局公用方法
│   ├── vendor                 # 公用vendor
│   ├── views                  # views 所有页面
│   ├── App.vue                # 入口页面
│   ├── main.js                # 入口文件 加载组件 初始化等
│   └── permission.js          # 权限管理
├── tests                      # 测试
├── .env.xxx                   # 环境变量配置
├── .eslintrc.js               # eslint 配置项
├── .babelrc                   # babel-loader 配置
├── .travis.yml                # 自动化CI配置
├── vue.config.js              # vue-cli 配置
├── postcss.config.js          # postcss 配置
└── package.json               # package.json
```

#### 2)安装依赖

要在==package.json所在的目录==下执行

```sh
npm config set registry http://registry.npm.taobao.org/
npm install
```

#### 3)运行测试

```sh
npm run dev
```

![](https://pic1.imgdb.cn/item/678e141bd0e0a243d4f5e478.png)

#### 4)配置修改

*以下在<u>vue.config.js</u>中进行修改*

- ##### 修改端口

此处修改为<u>8888</u>

![](https://pic1.imgdb.cn/item/678e1683d0e0a243d4f5e6cb.png)

- 关闭语法检查

此处修改为<u>false</u>

![](https://pic1.imgdb.cn/item/678e170dd0e0a243d4f5e738.png)

- 自动打开浏览器

此处改为<u>false</u>

39行为模拟测试，暂时先不用管。

![](https://pic1.imgdb.cn/item/678e1778d0e0a243d4f5e74c.png)

------

*以下在<u>src/settings.js</u>中进行修改*

显示在浏览器中的标题

![](https://pic1.imgdb.cn/item/678e18fed0e0a243d4f5e7d5.png)

------

*以下在<u>src/router/index.js</u>中进行修改*

![](https://pic1.imgdb.cn/item/678e1a33d0e0a243d4f5e84a.png)

==修改后需要重新部署项目才会生效==

```sh
npm run dev
```

### 4.登录页修改

修改<u>src/views/login/index.vue</u>中的内容

——将英文改为中文

![](https://pic1.imgdb.cn/item/678e1d4ed0e0a243d4f5ea94.png)

修改<u>src/utils/validate.js</u>

——取消用户名写死

![](https://pic1.imgdb.cn/item/678e209dd0e0a243d4f5eb77.png)

### 5.下拉菜单修改

修改<u>src/layout/components/Navbar.vue</u>中相应内容

### 6.图标

网站：https://www.iconfont.cn/

### 7.功能一：图书信息录入

#### 1)页面

##### i)代码：

```vue
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
```

##### ii)前端设计如下：

![](https://pic1.imgdb.cn/item/6791bc8ed0e0a243d4f6ff78.png)

##### iii)测试：

![](https://pic1.imgdb.cn/item/6791bd42d0e0a243d4f6ffaa.png)

#### 2)扫描图书部分

##### i)代码

```vue
<script>
export default {
  data() {
    return {
      form: {
        title: '',
        author: '',
        isbn: '',
        introduction: ''
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
      this.$axios.post('http://localhost:9999/book/ocr', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then(response => {
        // 便于检查
        console.log('识别结果:', response.data);

        // { title: 'Book Name', author: 'Author Name', isbn: 'ISBN Number', introduction: 'Book Introduction'}
        // 此处应为response.data.data!!!
        const bookInfo = response.data.data;

        // 填充表单
        this.fillBookInfo(bookInfo);
      }).catch(error => {
        console.error('识别失败:', error);
        this.$message.error('图书信息识别失败,请手动填写')
      });
    },
    fillBookInfo(bookInfo) {
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
    }
  }
}
</script>
```

##### ii)测试

向后端发出/book/ocr

![](https://pic1.imgdb.cn/item/67af186ad0e0a243d4ff34cd.png)

##### iii)加入加载功能

在对应表单中加入*v-loading*属性，然后在上传函数以及填充函数中修改loading值即可实现。

```vue
<el-form ref="form" :model="form" label-width="120px" v-loading="loading">
```

#### 3)提交部分

##### i)在书名、作者、isbn号加入判断是否为空

```vue
<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px" v-loading="loading">
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
```

##### ii)函数

```vue
<script>
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

      // 发送请求到后端
      this.$axios.post('http://localhost:9999/book/add', bookData)
        .then(response => {
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
        })
        .catch(error =>{
          console.error('录入失败:', error);
          this.$message.error('图书录入失败,请检查网络或稍后再试');
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
.error-message {
  color: red;
  font-size: 12px;
  margin-top: 5px;
}
</style>
```

#### 4)改进

##### i)前端发出请求

​	在测试过程中，发现后端正确发出了错误码，前端也正确接收到了错误码，但是仍然反馈录入成功，由此判断前端处理响应码出现了问题。通过阅读文档发现vue-element-admin一个完整的前端UI交互到服务端处理流程如下:

1. UI 组件交互操作；
2. 调用统一管理的 api service 请求函数；
3. 使用封装的 request.js 发送请求；
4. 获取服务端返回；
5. 更新 data；

###### a)在src/api下创建book.js

![](https://pic1.imgdb.cn/item/67b03572d0e0a243d4ff8e04.png)

```js
import request from '@/utils/request'

export function ocr(data) {
    return request({
        url: '/book/ocr',
        method: 'post',
        data
    })
} 

export function addBook(data) {
    return request({
        url: '/book/add',
        method: 'post',
        data
    })
}
```

###### b)修改views/form/index.vue代码

修改前:

```vue
// 发送请求到后端进行文字识别
this.$axios.post('http://localhost:9999/book/ocr', formData, {
    headers: {
    'Content-Type': 'multipart/form-data'
}

const bookInfo = response.data.data;

// 发送请求到后端
this.$axios.post('http://localhost:9999/book/add', bookData)
```

修改后:

```vue
// 发送请求到后端进行文字识别
ocr(formData)

const bookInfo = response.data;

// 发送请求到后端录入图书
addBook(bookData)
```

##### ii)图书扫描，点击取消后，表单清空，但是再次点扫描，无反应

<u>取消后表单清空，如果上传的是同一份文件，则无反应，如果上传的是另一份文件，却可以正常反应。</u>

*原因:handleFileUpload方法中确实在处理文件后没有重置input的值。因此，当用户再次选择同一个文件时，由于input的值没有变化，change事件不会被触发，导致无法发送请求。*

*解决方法:在处理完文件后，手动清空input的value，这样下次选择同一文件时，change事件会再次触发。*

```vue
// 发送请求到后端进行文字识别
ocr(formData).then(response => {
    // 重置文件输入
    event.target.value = ''; // 新增此行!!
	-- 原有代码 --
    ...
}).catch(error => {
    // 重置文件输入
    event.target.value = ''; // 新增此行!!
	-- 原有代码 --
    ...
  });
}


// 取消
onCancel() {
  this.$confirm('确定要放弃当前编辑吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    this.form = this.$options.data().form;
    // 重置文件输入
    this.$refs.fileInput.value = ''; // 新增此行!!
        
    this.$message.info({
      message: '已取消编辑',
      type: 'warning'
    });
  });
}
```

### 8.功能二：图书管理

#### 1)文件结构

- [x] 对前端文件进行了优化，删除了用不到的一些页面(主要是views文件夹下)。

- [x] 在views文件下新建了sys、statistic文件夹，依次对应了功能中的图书管理、统计分析。并将先前的图书信息录入页面重命名为add.vue并放入sys文件夹中，使得前端的文件结构更加清晰明了。
- [x] 在sys文件夹新增了book.vue、detail.vue，依次对应后续的图书管理、图书详情页面，并对<u>src/router/index.js</u>进行修改，修改如下：（其中的图书详情多设置了hidden:true参数）

```js
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'home' }
    }]
  },


  {
    path: '/sys',
    component: Layout,
    redirect: '/sys/add',
    name: 'sys',
    meta: { title: '图书管理', icon: 'form' },
    children: [
      {
        path: 'add',
        name: 'add',
        component: () => import('@/views/sys/add'),
        meta: { title: '图书录入', icon: 'add' }
      },
      {
        path: 'book',
        name: 'book',
        component: () => import('@/views/sys/book'),
        meta: { title: '图书管理', icon: 'table' }
      },
      {
        path: 'detail',
        name: 'detail',
        hidden: true,
        component: () => import('@/views/sys/detail'),
        meta: { title: '图书详情', icon: 'form'}
      }
    ]
  },

  {
    path: '/statistic',
    component: Layout,
    redirect: '/statistic',
    children: [{
      path: 'statistic',
      name: 'statistic',
      component: () => import('@/views/statistic/index'),
      meta: { title: '统计分析', icon: 'math' }
    }]
  }, 

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://github.com/jiav-yi/LibraryInfoSystem',
        meta: { title: '帮助', icon: 'link' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]
```

#### 2)前端发出请求

前文中的写法，在导入时需要将函数名也写上，这样当函数过多时，十分的不方便，因此做出了改进，并新增了后续会用到的新请求。

<u>src/api/book.js</u>

```js
export default {
    ocr(data) {
      return request({
        url: '/book/ocr',
        method: 'post',
        data
      });
    },
    addBook(data) {
      return request({
        url: '/book/add',
        method: 'post',
        data
      });
    },
    getBookList(searchModel) {
      return request({
        url: '/book/list',
        method: 'get',
        params: {
          pageNo: searchModel.pageNo,
          pageSize: searchModel.pageSize,
          title: searchModel.title,
          author: searchModel.author,
          isbn: searchModel.isbn
        }
      });
    },
    updateBook(book) {
        return request({
            url: '/book',
            method: 'put',
            data: book
        });
    },
    getBookByIsbn(isbn) {
        return request({
            // url: '/book/' + isbn,
            url: `/book/${isbn}`,
            method: 'get'
        });
    },
};
```

这种写法，后续导入时只需要

```vue
// 导入
import bookApi from '@/api/book'

// 使用
bookApi.XXX
```

#### 3)图书管理

##### i)book.vue

<template>部分主要实现了根据书名、isbn、作者的查询、图书的列表显示、分页组件、编辑时的对话框功能

<script>部分主要实现了分页、查询、对话框、修改、跳转详情页的功能

<style>部分主要是对搜索框和对话框进行了美化

```vue
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
```

并在App.vue中加入的部分美化

```vue
<style>
.app-main{
  padding: 10px;
}
.el-card{
  margin-bottom: 10px;
}
</style>
```

##### ii)显示

![](https://pic1.imgdb.cn/item/67b3f853d0e0a243d40060e2.png)

![](https://pic1.imgdb.cn/item/67b3f956d0e0a243d4006144.png)

#### 4)图书详情

##### i)detail.vue

```vue
<template>
  <div class="app-container">
    <!-- 图书详情 -->
    <el-card>
      <div class="book-details">
        <!-- 图书封面 -->
        <div class="book-image">
          <img :src="book.image" alt="图书封面" />
        </div>

        <!-- 图书信息 -->
        <div class="book-info">
          <h2 class="book-title">{{ book.title }}</h2>
          <p class="book-introduction">{{ book.introduction }}</p>

          <!-- 图书详情 -->
          <el-row :gutter="20">
            <el-col :span="20">
              <p><strong>作者：</strong>{{ book.author }}</p>
            </el-col>
            <el-col :span="20">
              <p><strong>ISBN：</strong>{{ book.isbn }}</p>
            </el-col>
          </el-row>

          <!-- 评分及推荐部分 -->
          <div class="rating-box">
            <div class="score">
              <span><strong>评分：</strong></span>
              <span class="score-number">{{ parsedComments.rating.split('/')[0] }}</span>
              <span class="score-total">/10</span>
              <span class="recommend-text">{{ parsedComments.recommendation }}</span>
            </div>
          </div>
          
          <!-- 评论部分 -->
          <div class="book-comments">
            <h3 class="comment-title"><i class="el-icon-chat-line-round"></i> 深度书评分析</h3>
            
            <!-- 结构化评论容器 -->
            <div class="comment-structure">
              <!-- 正面评价 -->
              <el-card class="comment-section positive">
                <div slot="header" class="comment-header">
                  <el-tag type="success" effect="dark">👍 核心优势</el-tag>
                </div>
                <ul class="comment-list">
                  <li v-for="(item, index) in parsedComments.positive" :key="'positive'+index">
                    <h4 class="aspect-title">{{ item.title }}</h4>
                    <p class="aspect-content">{{ item.content }}</p>
                  </li>
                </ul>
              </el-card>

              <!-- 负面评价 -->
              <el-card class="comment-section negative">
                <div slot="header" class="comment-header">
                  <el-tag type="danger" effect="dark">👎 不足之处</el-tag>
                </div>
                <ul class="comment-list">
                  <li v-for="(item, index) in parsedComments.negative" :key="'negative'+index">
                    <h4 class="aspect-title">{{ item.title }}</h4>
                    <p class="aspect-content">{{ item.content }}</p>
                  </li>
                </ul>
              </el-card>

              <!-- 综合评估 -->
              <el-card class="comprehensive-assessment">
                <div slot="header" class="comment-header">
                  <el-tag type="warning" effect="dark">🔍 综合评估</el-tag>
                </div>
                <ul class="comment-list">
                  <li v-for="(item, index) in parsedComments.comprehensive" :key="'comprehensive'+index">
                    <h4 class="aspect-title">{{ item.title }}</h4>
                    <p class="aspect-content">{{ item.content }}</p>
                  </li>
                </ul>
              </el-card>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import bookApi from '@/api/book'

export default {
  data() {
    return {
      book: {
        title: '',
        author: '',
        isbn: '',
        introduction: '',
        comments: '',
        image: ''
      },
    }
  },
  computed: {
    formattedComments() {
      if (!this.book.comments) {
        return '';
      }
      return this.book.comments.replace(/\\n/g, '<br>');
    },
    parsedComments() {
      const result = {
        positive: [],
        negative: [],
        comprehensive: [],
        rating: '',
        recommendation: ''
      };

      if (typeof this.book.comments === 'string') {
        // 如果comments是字符串，解析为对象
        this.book.comments = JSON.parse(this.book.comments);
      }

      if (this.book.comments) {
        result.positive = this.book.comments.positive || [];
        result.negative = this.book.comments.negative || [];
        result.comprehensive = this.book.comments.comprehensive || [];
        result.rating = this.book.comments.rating || '';
        result.recommendation = this.book.comments.recommendation || '';
      }

      return result;
    }
  },
  created() {
    const bookData = this.$route.query.book;
    if (bookData) {
      this.book = JSON.parse(bookData);
    }
  }
}
</script>

<style scoped>
.book-details {
  display: flex;
  align-items: flex-start;
}

.book-image {
  flex: 1;
  margin-right: 20px;
}

.book-image img {
  max-width: 200px;
  border-radius: 8px;
}

.book-info {
  flex: 3;
}

.book-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.book-introduction {
  margin-bottom: 20px;
  line-height: 1.6;
}

.book-comments {
  margin-top: 20px;
}

.book-comments h3 {
  font-size: 18px;
  margin-bottom: 10px;
}

.book-comments p {
  line-height: 1.6;
}

/* 新增样式 */
.comment-title {
  font-size: 1.5rem;
  color: #2c3e50;
  border-bottom: 2px solid #409EFF;
  padding-bottom: 0.5rem;
  margin-bottom: 1.5rem;
}

.comment-structure {
  display: grid;
  gap: 1.5rem;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
}

.comment-section {
  transition: transform 0.3s;
  border-top: 3px solid transparent;
}

.comment-section:hover {
  transform: translateY(-5px);
}

.positive {
  border-top-color: #67C23A;
}

.negative {
  border-top-color: #F56C6C;
}

/*  */
.positive, .negative, .comprehensive-assessment {
  width: 100%;
}
.comprehensive-assessment {
  border-top-color: #E6A23C;
  grid-column: 1 / -1;
}
/*  */


.aspect-title {
  color: #409EFF;
  font-size: 1.1rem;
  margin: 0.8rem 0;
  padding-left: 1rem;
  border-left: 3px solid #409EFF;
}

.aspect-content {
  line-height: 1.8;
  color: #606266;
  text-align: justify;
}

.rating-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.score-number {
  font-size: 2.5rem;
  color: #F56C6C;
  font-weight: bold;
}

.score-total {
  font-size: 1.2rem;
  color: #909399;
}

.recommend-tag {
  font-size: 1.1rem;
  padding: 8px 15px;
}

.recommend-text {
  font-size: 0.95rem;
  color: #5a5e66;
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 4px;
}

.assessment-card {
  margin-bottom: 1rem;
}

.assessment-card h4 {
  color: #5a5e66;
  margin-bottom: 1rem;
}

@media (max-width: 768px) {
  .comment-structure {
    grid-template-columns: 1fr;
  }
  
  .rating-box {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
```

##### ii)显示

![](https://pic1.imgdb.cn/item/67b3fb61d0e0a243d4006203.jpg)

### 9.功能三：统计分析

#### 1)准备工作

安装ECharts

```sh
npm install echarts --save
```

#### 2)前端发出请求

src/api/statistic.js

```js
import { title } from '@/settings'
import request from '@/utils/request'

export default {
    getStatisticData() {
        return request({
            url: '/statistic/data',
            method: 'get'
        });
    }
}
```

#### 3)统计分析

##### i)statistic.vue

由于本项目使用的大语言模型为免费版，难以生成采购报告，因此此处写死

```vue
<template>
  <div class="dashboard" v-loading="loading">
    <el-container style="margin-left: -20px">
      <el-container>
        <el-header height="120px">
          <el-row :gutter="20" class="mb-20">
            <el-col :span="12">
              <el-card class="stat-card">
                <div slot="header">
                  <i class="el-icon-sell"></i> <strong>总销量</strong>
                </div>
                <div class="stat-value">{{ saleTotal }}</div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card class="stat-card">
                <div slot="header">
                  <i class="el-icon-document"></i> <strong>图书种类</strong>
                </div>
                <div class="stat-value">{{ bookTotal }}</div>
              </el-card>
            </el-col>
          </el-row>
        </el-header>
        <el-main>
          <el-card>
            <!-- <div slot="header">数据库中热销图书关键词</div> -->
            <div id="keywordChart" style="width: 100%; height: 250px;"></div>
          </el-card>
        </el-main>
      </el-container>
      <el-aside width="593px">
        <el-card>
          <div slot="header"><strong>热销图书</strong></div>
          <el-table :data="hotBooks" border style="width: 100%; height: 335px;">
            <el-table-column prop="rank" label="排名" width="50"></el-table-column>
            <el-table-column prop="title" label="书名"></el-table-column>
            <el-table-column prop="author" label="作者"></el-table-column>
            <el-table-column prop="rating" label="评分"></el-table-column>
            <el-table-column prop="sales" label="销量"></el-table-column>
          </el-table>
        </el-card>
      </el-aside>
    </el-container>

    <!-- 词云图、扇形图 -->
    <el-row :gutter="12" class="mb-20">
      <el-col :span="6">
        <el-card style="height: 250px;">
          <div id="wordCloudChart" style="width: 100%; height: 250px;"></div>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card class="suggestion-section" style="height: 250px; overflow-y: auto;">
          <div slot="header" class="suggetion-header">
            <div slot="header"><strong>采购分析报告</strong></div>
          </div>
          <div class="report-container">
            <!-- 最畅销类别 -->
            <div class="report-section">
              <div class="section-header">
                <el-tag type="success" class="tag-icon"><i class="el-icon-trophy"></i></el-tag>
                <h3>最畅销类别</h3>
              </div>
              <div class="section-content">
                <div v-for="(item, index) in purchaseReport.categories" :key="'category'+index" class="report-item">
                  <div class="item-header">
                    <el-tag size="mini" type="info">{{ index+1 }}</el-tag>
                    <strong class="item-title">{{ item.category }}</strong>
                    <el-tag size="mini" effect="plain">示例：{{ item.examples }}</el-tag>
                  </div>
                  <p class="item-desc">{{ item.description }}</p>
                </div>
              </div>
            </div>

            <!-- 最畅销作者 -->
            <div class="report-section">
              <div class="section-header">
                <el-tag type="warning" class="tag-icon"><i class="el-icon-user-solid"></i></el-tag>
                <h3>最畅销作者</h3>
              </div>
              <div class="section-content">
                <div v-for="(item, index) in purchaseReport.authors" :key="'author'+index" class="report-item">
                  <div class="item-header">
                    <el-tag size="mini" type="info">{{ index+1 }}</el-tag>
                    <strong class="item-title">{{ item.name }}</strong>
                    <el-tag size="mini" type="success">代表作：{{ item.works }}</el-tag>
                  </div>
                  <p class="item-desc">{{ item.description }}</p>
                </div>
              </div>
            </div>

            <!-- 采购建议 -->
            <div class="report-section">
              <div class="section-header">
                <el-tag type="danger" class="tag-icon"><i class="el-icon-lightbulb"></i></el-tag>
                <h3>采购建议</h3>
              </div>
              <div class="section-content">
                <div v-for="(item, index) in purchaseReport.suggestions" :key="'suggestion'+index" class="report-item">
                  <el-alert :title="item.title" type="info" :closable="false" class="suggestion-alert">
                    {{ item.content }}
                  </el-alert>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { ElMessage } from "element-ui";
import "@/utils/echarts-wordcloud.min.js"
import statisticApi from "@/api/statistic";

export default {
  data() {
    return {
      loading: true,
      saleTotal: 0, // 总销量
      bookTotal: 0, // 图书量
      hotBooks: [], // 热销图书数据
      keywordData: [], // 关键词数据
      pieChartData: [], // 扇形图数据
      purchaseReport: {
                        categories: [
                          {
                            category: "魔幻现实主义文学",
                            examples: "《百年孤独》《三体》",
                            description: "这些作品以其独特的叙事手法和深刻的主题，吸引了大量读者。"
                          },
                          {
                            category: "中国古典四大名著",
                            examples: "《红楼梦》",
                            description: "作为中国文学史上的瑰宝，其深厚的文化内涵和丰富的人物形象，使其成为畅销书的常客。"
                          },{
                            category: "科幻小说",
                            examples: "《三体》《人类简史》",
                            description: "这两部作品在科幻与历史结合上取得了巨大成功，吸引了广泛的读者群体。"
                          }
                        ],
                        authors: [
                          {
                            name: "刘慈欣",
                            works: "《三体》系列",
                            description: "中国科幻文学代表人物，全球范围内享有极高声誉。"
                          },
                          {
                            name: "乔治·奥威尔",
                            works: "《1984》",
                            description: "这部反乌托邦小说，展示了其深邃的政治和社会洞察力。"
                          },
                          {
                            name: "安托万·德·圣-埃克苏佩里",
                            works: "《小王子》",
                            description: "其作品富有哲理性和艺术性，深受全球读者喜爱。"
                          },
                        ],
                        suggestions: [
                          {
                            title: "类别采购建议",
                            content: "选择高销量高评价小说，尤其是结合魔幻元素、中国古典文学或科幻题材的作品。"
                          },
                          {
                            title: "作者采购建议",
                            content: "优先选择那些具有广泛知名度和影响力的作者的作品，以确保图书的市场接受度。"
                          },
                          {
                            title: "目标读者群采购建议",
                            content: "考虑到不同年龄段和文化背景的读者需求，可以选择一些既有深度又具娱乐性的图书，以满足不同读者的需求。"
                          }
                        ]
                      } // 采购报告
    };
  },
  mounted() {
    this.getStatisticData(); // 调用方法获取数据
  },
  methods: {
    // 获取数据
    getStatisticData() {
      statisticApi.getStatisticData().then(response => {
        // 设置数据
        this.saleTotal = response.data.saleTotal;
        this.bookTotal = response.data.bookTotal;
        this.hotBooks = response.data.hotBooks;
        this.keywordData = response.data.keywordData || [];
        this.pieChartData = response.data.pieChartData || [];

        // 重新渲染图标
        this.$nextTick(() => {
          this.initKeywordChart();
          this.initWordCloudChart();
          this.loading = false;
        });
      }).catch(error => {
        console.error("获取数据失败:", error);
        ElMessage.error("获取数据失败，请稍后再试！");
      })
    },
    // 初始化关键词热度图
    initKeywordChart() {
      const chartDom = document.getElementById("keywordChart");
      const myChart = echarts.init(chartDom);
      const option = {
        title: {
          text: "关键词热度",
        },
        tooltip: {},
        xAxis: {
          type: "category",
          data: this.keywordData.map(item => item.name),
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: "热度",
            type: "bar",
            data: this.keywordData.map(item => item.value),
          },
        ],
      };
      myChart.setOption(option);
    },
    // 初始化词云图
    initWordCloudChart() {
      const chartDom = document.getElementById("wordCloudChart");
      const myChart = echarts.init(chartDom);
      const option = {
        title: {
          text: "词云图",
        },
        tooltip: {},
        series: [
          {
            type: "wordCloud",
            shape: "circle",
            sizeRange: [10, 50],
            rotationRange: [-90, 90],
            data: this.keywordData,
          },
        ],
      };
      myChart.setOption(option);
    }
  },
};
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

/* 采购建议样式 */
.suggestion-section {
  margin-bottom: 20px;
}

.suggestion-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.suggestion-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.suggestion-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}

.suggestion-content {
  font-size: 14px;
  color: #666;
}

/* 采购报告 */
.report-container {
  padding: 10px;
}

.section-header {
  display: flex;
  align-items: center;
  margin: 15px 0 10px;
  padding-bottom: 5px;
  border-bottom: 1px solid #eee;
}

.tag-icon {
  margin-right: 10px;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.report-item {
  margin: 12px 0;
  padding: 10px;
  border-left: 3px solid #409EFF;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.item-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.item-title {
  margin: 0 10px;
  color: #303133;
}

.item-desc {
  margin: 0;
  color: #606266;
  font-size: 13px;
  line-height: 1.6;
}

.suggestion-alert {
  margin: 8px 0;
  padding: 10px;
}
</style>
```

##### ii)显示

![](https://pic1.imgdb.cn/item/67b74633d0e0a243d4011465.png)





## 后端

### 1.项目初始化

Java版本：1.8

Spring Boot版本：2.7.6

pom依赖：

```xml
<!-- web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!-- mysql -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
<!-- mybatis-plus -->
<dependency>
	<groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.2</version>
</dependency>
<dependency>
	<groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>3.5.2</version>
</dependency>
<!-- freemarker -->
<dependency>
	<groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
</dependency>
<!-- lombok -->
<dependency>
	<groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

==在idea设置中找到maven，修改相应配置==

![](https://pic1.imgdb.cn/item/6793139bd0e0a243d4f76c90.png)

配置<u>application.yml</u>文件

![](https://pic1.imgdb.cn/item/6795ae09d0e0a243d4f8039c.png)

```yaml
server:
  port: 9999

spring:
  datasource:
    username: root
    password: zjwzjw123
    url: jdbc:mysql:///book_db
  redis:
    port: 6379
    host: localhost

logging:
  level:
    com.jnu: debug
```

==配置完之后运行main函数，看下是否有报错。==



### 2.Mybatis-plus代码生成

#### 1)编写代码生成器

*CodeGenerator.java文件建立在<u>test/com.jnu/</u>下，因为该代码不需要进行打包*

==参数要对应进行修改==

```java
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql:///book_db";
        String username = "root";
        String password = "zjwzjw123";
        String author = "jiav";
        String outputDir = "E:\\Programme\\graduation\\LibraryInfoSystem-back\\LibraryInfoSystem-back\\src\\main\\java";
        String basePackage = "com.jnu";
        String moduleName = "sys";
        String mapperLocation = "E:\\Programme\\graduation\\LibraryInfoSystem-back\\LibraryInfoSystem-back\\src\\main\\resources\\mapper\\" + moduleName;
        String tableName = "book";
//        String tablePrefix = "x_";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(basePackage) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableName); // 设置需要生成的表名
//                            .addTablePrefix(tablePrefix); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
```

运行结果:

![](https://pic1.imgdb.cn/item/6795c203d0e0a243d4f806b2.png)

![](https://pic1.imgdb.cn/item/6795c231d0e0a243d4f806b6.png)

==如果对数据库进行了修改，想重新生成代码，但又不想覆盖原有代码==

![](https://pic1.imgdb.cn/item/67b1a018d0e0a243d4ffca30.png)



#### 2)在启动函数中添加注解

```java
@MapperScan("com.jnu.*.mapper")
```

#### 3)测试

##### i)在test中编写测试mapper的代码

```java
@SpringBootTest
class LibraryInfoSystemBackApplicationTests {

    @Resource
    private BookMapper bookMapper;

    @Test
    void testMapper() {
        List<Book> book = bookMapper.selectList(null);
        book.forEach(System.out::println);
    }
}
```

##### ii)在数据库中插入一条数据

![](https://pic1.imgdb.cn/item/6795e3f6d0e0a243d4f80d19.png)

##### iii)运行测试代码

![](https://pic1.imgdb.cn/item/6795e441d0e0a243d4f80d46.png)

##### iv)测试Controller

![](https://pic1.imgdb.cn/item/6795e73dd0e0a243d4f80df0.png)

![](https://pic1.imgdb.cn/item/6795e9bed0e0a243d4f80e84.png)

### 3.公共响应类

#### 1)创建公共响应类

![](https://pic1.imgdb.cn/item/6795edd5d0e0a243d4f80f61.png)

#### 2)公共响应类代码

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static<T>  Result<T> success(){
        return new Result<>(20000,"success",null);
    }

    public static<T>  Result<T> success(T data){
        return new Result<>(20000,"success",data);
    }

    public static<T>  Result<T> success(T data, String message){
        return new Result<>(20000,message,data);
    }

    public static<T>  Result<T> success(String message){
        return new Result<>(20000,message,null);
    }

    public static<T>  Result<T> fail(){
        return new Result<>(20001,"fail",null);
    }

    public static<T>  Result<T> fail(Integer code){
        return new Result<>(code,"fail",null);
    }

    public static<T>  Result<T> fail(Integer code, String message){
        return new Result<>(code,message,null);
    }

    public static<T>  Result<T> fail( String message){
        return new Result<>(20001,message,null);
    }
}
```

#### 3)测试

```java
@GetMapping("/all")
public Result<List<Book>> getAllBooks() {
    List<Book> list = bookService.list();
    return Result.success(list, "查询成功");
}
```

测试结果

```json
{
  "code": 20000,
  "message": "查询成功",
  "data": [
    {
      "isbn": "9787020008728",
      "title": "三国演义",
      "author": "罗贯中",
      "introduction": "描写了从东汉末年到西晋初年之间近105年的历史风云，以描写战争为主，反映了东汉末年的群雄割据混战和魏、蜀、吴三国之间的政治和军事斗争。",
      "comments": null,
      "image": null,
      "stock": 0
    }
  ]
}
```

### 4.功能一：图书信息录入

| 接口属性 | 值                                                           |
| :------- | ------------------------------------------------------------ |
| url      | /book/ocr                                                    |
| method   | post                                                         |
| 请求参数 | multipart/form-data                                          |
| 返回参数 | ![](https://pic1.imgdb.cn/item/67aec57cd0e0a243d4ff0cef.png) |

#### 1)编写代码框架

![](https://pic1.imgdb.cn/item/67a480cad0e0a243d4fc3af0.png)

![](https://pic1.imgdb.cn/item/67a48119d0e0a243d4fc3aff.png)

#### 2)初步测试

利用Apifox进行初步测试

![](https://pic1.imgdb.cn/item/67a48167d0e0a243d4fc3b09.png)

![image-20250206173240077](C:\Users\86135\AppData\Roaming\Typora\typora-user-images\image-20250206173240077.png)

#### 3)调用百度OCR文字识别

后续将filePath更换为从前端传来的图片路径

```java
    /**
     * 文字识别图书
     * @return
     */
    @Override
    public Map<String, Object> ocr() {

        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";

        try {
            // 本地文件路径
            String filePath = "E:\\Programme\\graduation\\LibraryInfoSystem-back\\LibraryInfoSystem-back\\src\\main\\java\\com\\jnu\\pic\\test.png";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.73e9c81fa2fb1a4dc3ab5a20b2306349.2592000.1741601729.282335-117382515";

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);

            JSONObject jsonObject = JSON.parseObject(result);
            Map<String, Object> result_map = (Map<String, Object>)jsonObject;

            return result_map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
//        // test code
//        String key = "test1";
//        if (key == "test") {
//            Map<String, Object> data = new HashMap<>();
//            data.put("token", key);
//            return data;
//        }
    }
```

#### 4)测试

==后续应处理json串，提取作者、书名等关键信息。==

![](https://pic1.imgdb.cn/item/67a74842d0e0a243d4fd146e.png)

#### 5)改进

在4)中提及应处理json串，提取作者、书名等关键信息

有三套方案：

| 第一种：利用自然语言处理从json串中提取关键信息 |
| ---------------------------------------------- |
| 第二种：利用json串中的信息进行爬虫             |
| 第三种：利用大语言模型获取关键信息             |

==鉴于第三种实现简单、直接，本文采用第三种方案：利用大语言模型获取关键信息==

#### 6)配置大语言模型

***参考:[讯飞星火认知大模型Java后端接口-CSDN博客](https://blog.csdn.net/qq_62982856/article/details/133151673)***

##### i)选取了讯飞星火大语言模型Lite版本(该版本免费)

进行实名认证、创建应用、获取相关api信息

##### ii)加入相关依赖

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.5</version>
</dependency>
<dependency>
    <groupId>org.java-websocket</groupId>
    <artifactId>Java-WebSocket</artifactId>
    <version>1.3.8</version>
</dependency>
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.10.0</version>
</dependency>
<dependency>
    <groupId>com.squareup.okio</groupId>
    <artifactId>okio</artifactId>
    <version>2.10.0</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.8.18</version>
</dependency>
```

##### iii)application.yml配置文件

```yaml
model:
  # 服务引擎使用 讯飞星火认知大模型V2.0，如果使用 V1.5 需要将 hostUrl 修改为 https://spark-api.xf-yun.com/v1.1/chat
  hostUrl: https://spark-api.xf-yun.com/v1.1/chat
  # 发送请求时指定的访问领域，如果是 V1.5版本 设置为 general，如果是 V2版本 设置为 generalv2
  domain: lite
  # 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高。取值 [0,1]
  temperature: 0.5
  # 模型回答的tokens的最大长度，V1.5取值为[1,4096]，V2.0取值为[1,8192]。
  maxTokens: 2048
  # 大模型回复问题的最大响应时长，单位 s
  maxResponseTime: 30
  # 允许同时连接大模型的 websocket 数，如果是普通（免费）用户为 2，超过这个数连接响应会报错，具体参考官网。
  QPS: 2
  # 用于权限验证，从服务接口认证信息中获取
  appId: 
  # 用于权限验证，从服务接口认证信息中获取
  apiKey: 
  # 用于权限验证，从服务接口认证信息中获取
  apiSecret: 
```

##### iv)config包

![](https://pic1.imgdb.cn/item/67ad72afd0e0a243d4fec1a3.png)

```java
@Configuration
@ConfigurationProperties(prefix = "model")
@Data
public class BigModelConfig {
    /**
     * 服务引擎使用 讯飞星火认知大模型V2.0，如果使用 V1.5 需要将 hostUrl 修改为 https://spark-api.xf-yun.com/v1.1/chat
     */
    private String hostUrl;
    /**
     * 发送请求时指定的访问领域，如果是 V1.5版本 设置为 general，如果是 V2版本 设置为 generalv2
     */
    private String domain;
    /**
     * 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高。取值 [0,1]
     */
    private Float temperature;
    /**
     * 模型回答的tokens的最大长度，V1.5取值为[1,4096]，V2.0取值为[1,8192]。
     */
    private Integer maxTokens;
    /**
     * 大模型回复问题的最大响应时长，单位 s
     */
    private Integer maxResponseTime;
    /**
     * 用于权限验证，从服务接口认证信息中获取
     */
    private String appId;
    /**
     * 用于权限验证，从服务接口认证信息中获取
     */
    private String apiKey;
    /**
     * 用于权限验证，从服务接口认证信息中获取
     */
    private String apiSecret;
}
```

##### v)dto包

![](https://pic1.imgdb.cn/item/67ad735dd0e0a243d4fec1b1.png)

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgDTO {
    /**
     * 角色
     */
    private String role;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 响应结果字段：结果序号，取值为[0,10]; 当前为保留字段，开发者可忽略
     */
    private Integer index;

    public static final String ROLE_USER = "user";
    public static final String ROLE_ASSISTANT = "assistant";

    public static MsgDTO createUserMsg(String content) {
        return new MsgDTO(ROLE_USER, content, null);
    }

    public static MsgDTO createAssistantMsg(String content) {
        return new MsgDTO(ROLE_ASSISTANT, content, null);
    }
}
```

```java
/**
 * 请求参数
 * 对应生成的 JSON 结构参考 resources/demo-json/request.json
 *
 * @author jiav
 * @create 2025-02-12 21:23
 */
@NoArgsConstructor
@Data
public class RequestDTO {
    @JsonProperty("header")
    private HeaderDTO header;
    @JsonProperty("parameter")
    private ParameterDTO parameter;
    @JsonProperty("payload")
    private PayloadDTO payload;

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class HeaderDTO {
        /**
         * 应用appid，从开放平台控制台创建的应用中获取
         */
        @JSONField(name = "app_id")
        private String appId;
        /**
         * 每个用户的id，用于区分不同用户，最大长度32
         */
        @JSONField(name = "uid")
        private String uid;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class ParameterDTO {
        private ChatDTO chat;

        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        public static class ChatDTO {
            /**
             * 指定访问的领域,general指向V1.5版本 generalv2指向V2版本。注意：不同的取值对应的url也不一样！
             */
            @JsonProperty("domain")
            private String domain;
            /**
             * 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高
             */
            @JsonProperty("temperature")
            private Float temperature;
            /**
             * 模型回答的tokens的最大长度
             */
            @JSONField(name = "max_tokens")
            private Integer maxTokens;
        }
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class PayloadDTO {
        @JsonProperty("message")
        private MessageDTO message;

        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        public static class MessageDTO {
            @JsonProperty("text")
            private List<MsgDTO> text;
        }
    }
}
```

```java
/**
 * 返回参数
 * 对应生成的 JSON 结构参考 resources/demo-json/response.json
 *
 * @author jiav
 * @create 2025-02-12 21:24
 */
@NoArgsConstructor
@Data
public class ResponseDTO {
    @JsonProperty("header")
    private HeaderDTO header;
    @JsonProperty("payload")
    private PayloadDTO payload;

    @NoArgsConstructor
    @Data
    public static class HeaderDTO {
        /**
         * 错误码，0表示正常，非0表示出错
         */
        @JsonProperty("code")
        private Integer code;
        /**
         * 会话是否成功的描述信息
         */
        @JsonProperty("message")
        private String message;
        /**
         * 会话的唯一id，用于讯飞技术人员查询服务端会话日志使用,出现调用错误时建议留存该字段
         */
        @JsonProperty("sid")
        private String sid;
        /**
         * 会话状态，取值为[0,1,2]；0代表首次结果；1代表中间结果；2代表最后一个结果
         */
        @JsonProperty("status")
        private Integer status;
    }

    @NoArgsConstructor
    @Data
    public static class PayloadDTO {
        @JsonProperty("choices")
        private ChoicesDTO choices;
        /**
         * 在最后一次结果返回
         */
        @JsonProperty("usage")
        private UsageDTO usage;

        @NoArgsConstructor
        @Data
        public static class ChoicesDTO {
            /**
             * 文本响应状态，取值为[0,1,2]; 0代表首个文本结果；1代表中间文本结果；2代表最后一个文本结果
             */
            @JsonProperty("status")
            private Integer status;
            /**
             * 返回的数据序号，取值为[0,9999999]
             */
            @JsonProperty("seq")
            private Integer seq;
            /**
             * 响应文本
             */
            @JsonProperty("text")
            private List<MsgDTO> text;

        }

        @NoArgsConstructor
        @Data
        public static class UsageDTO {
            @JsonProperty("text")
            private TextDTO text;

            @NoArgsConstructor
            @Data
            public static class TextDTO {
                /**
                 * 保留字段，可忽略
                 */
                @JsonProperty("question_tokens")
                private Integer questionTokens;
                /**
                 * 包含历史问题的总tokens大小
                 */
                @JsonProperty("prompt_tokens")
                private Integer promptTokens;
                /**
                 * 回答的tokens大小
                 */
                @JsonProperty("completion_tokens")
                private Integer completionTokens;
                /**
                 * prompt_tokens和completion_tokens的和，也是本次交互计费的tokens大小
                 */
                @JsonProperty("total_tokens")
                private Integer totalTokens;
            }
        }
    }
}
```

##### vi)listener包

![](https://pic1.imgdb.cn/item/67ae072ad0e0a243d4fef3c1.png)

```java
@Slf4j
public class BigModelWebSocketListener extends WebSocketListener {
    private StringBuilder answer = new StringBuilder();

    private boolean wsCloseFlag = false;

    public StringBuilder getAnswer() {
        return answer;
    }

    public boolean isWsCloseFlag() {
        return wsCloseFlag;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        // 将大模型回复的 JSON 文本转为 ResponseDTO 对象
        ResponseDTO responseData = JSONObject.parseObject(text, ResponseDTO.class);
        // 如果响应数据中的 header 的 code 值不为 0，则表示响应错误
        if (responseData.getHeader().getCode() != 0) {
            // 日志记录
            log.error("发生错误，错误码为：" + responseData.getHeader().getCode() + "; " + "信息：" + responseData.getHeader().getMessage());
            // 设置回答
            this.answer = new StringBuilder("大模型响应错误，请稍后再试");
            // 关闭连接标识
            wsCloseFlag = true;
            return;
        }
        // 将回答进行拼接
        for (MsgDTO msgDTO : responseData.getPayload().getChoices().getText()) {
            this.answer.append(msgDTO.getContent());
        }
        // 对最后一个文本结果进行处理
        if (2 == responseData.getHeader().getStatus()) {
            wsCloseFlag = true;
        }
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
    }
}
```

##### vii)component包

![](https://pic1.imgdb.cn/item/67ae07a5d0e0a243d4fef3cd.png)

```java
@Component
@Slf4j
public class BigModelStreamClient {
    @Resource
    private BigModelConfig bigModelConfig;

    @Value("${model.QPS}")
    private int connectionTokenCount;

    /**
     * 获取令牌
     */
    public static int GET_TOKEN_STATUS = 0;
    /**
     * 归还令牌
     */
    public static int BACK_TOKEN_STATUS = 1;

    /**
     * 操作令牌
     *
     * @param status 0-获取令牌 1-归还令牌
     * @return 是否操作成功
     */
    public synchronized boolean operateToken(int status) {
        if (status == GET_TOKEN_STATUS) {
            // 获取令牌
            if (connectionTokenCount != 0) {
                // 说明还有令牌，将令牌数减一
                connectionTokenCount -= 1;
                return true;
            } else {
                return false;
            }
        } else {
            // 放回令牌
            connectionTokenCount += 1;
            return true;
        }
    }

    /**
     * 发送消息
     *
     * @param uid     每个用户的id，用于区分不同用户
     * @param msgList 发送给大模型的消息，可以包含上下文内容
     * @return 获取websocket连接，以便于我们在获取完整大模型回复后手动关闭连接
     */
    public WebSocket sendMsg(String uid, List<MsgDTO> msgList, WebSocketListener listener) {
        // 获取鉴权url
        String authUrl = this.getAuthUrl();
        // 鉴权方法生成失败，直接返回 null
        if (authUrl == null) {
            return null;
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        // 将 https/http 连接替换为 ws/wss 连接
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        // 建立 wss 连接
        WebSocket webSocket = okHttpClient.newWebSocket(request, listener);
        // 组装请求参数
        RequestDTO requestDTO = getRequestParam(uid, msgList);
        // 发送请求
        webSocket.send(JSONObject.toJSONString(requestDTO));
        return webSocket;
    }

    /**
     * 生成鉴权方法，具体实现不用关心，这是讯飞官方定义的鉴权方式
     *
     * @return 鉴权访问大模型的路径
     */
    public String getAuthUrl() {
        try {
            URL url = new URL(bigModelConfig.getHostUrl());
            // 时间
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = format.format(new Date());
            // 拼接
            String preStr = "host: " + url.getHost() + "\n" +
                    "date: " + date + "\n" +
                    "GET " + url.getPath() + " HTTP/1.1";
            // SHA256加密
            Mac mac = Mac.getInstance("hmacsha256");
            SecretKeySpec spec = new SecretKeySpec(bigModelConfig.getApiSecret().getBytes(StandardCharsets.UTF_8), "hmacsha256");
            mac.init(spec);

            byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
            // Base64加密
            String sha = Base64.getEncoder().encodeToString(hexDigits);
            // 拼接
            String authorizationOrigin = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", bigModelConfig.getApiKey(), "hmac-sha256", "host date request-line", sha);
            // 拼接地址
            HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().
                    addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorizationOrigin.getBytes(StandardCharsets.UTF_8))).
                    addQueryParameter("date", date).
                    addQueryParameter("host", url.getHost()).
                    build();

            return httpUrl.toString();
        } catch (Exception e) {
            log.error("鉴权方法中发生错误：" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取请求参数
     *
     * @param uid     每个用户的id，用于区分不同用户
     * @param msgList 发送给大模型的消息，可以包含上下文内容
     * @return 请求DTO，该 DTO 转 json 字符串后生成的格式参考 resources/demo-json/request.json
     */
    public RequestDTO getRequestParam(String uid, List<MsgDTO> msgList) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setHeader(new RequestDTO.HeaderDTO(bigModelConfig.getAppId(), uid));
        requestDTO.setParameter(new RequestDTO.ParameterDTO(new RequestDTO.ParameterDTO.ChatDTO(bigModelConfig.getDomain(), bigModelConfig.getTemperature(), bigModelConfig.getMaxTokens())));
        requestDTO.setPayload(new RequestDTO.PayloadDTO(new RequestDTO.PayloadDTO.MessageDTO(msgList)));
        return requestDTO;
    }
}
```

##### viii)utils包

将提问大语言模型过程写成一个工具类

![](https://pic1.imgdb.cn/item/67ae09edd0e0a243d4fef46e.png)

```java
@Slf4j
@Component
public class BigModelUtil {
    @Resource
    private BigModelStreamClient bigModelStreamClient;

    @Resource
    private BigModelConfig bigModelConfig;

    /**
     * 发送问题
     * @param question 问题
     * @return 星火大模型的回答
     */
    public String sendQuestion(String question) {
        // 如果是无效字符串，则不对大模型进行请求
        if (StrUtil.isBlank(question)) {
            return "无效问题，请重新输入";
        }
        // 获取连接令牌
        if (!bigModelStreamClient.operateToken(BigModelStreamClient.GET_TOKEN_STATUS)) {
            return "当前大模型连接数过多，请稍后再试";
        }

        // 创建消息对象
        MsgDTO msgDTO = MsgDTO.createUserMsg(question);
        // 创建监听器
        BigModelWebSocketListener listener = new BigModelWebSocketListener();
        // 发送问题给大模型，生成 websocket 连接
        WebSocket webSocket = bigModelStreamClient.sendMsg(UUID.randomUUID().toString().substring(0, 10), Collections.singletonList(msgDTO), listener);
        if (webSocket == null) {
            // 归还令牌
            bigModelStreamClient.operateToken(BigModelStreamClient.BACK_TOKEN_STATUS);
            return "系统内部错误，请联系管理员";
        }
        try {
            int count = 0;
            // 为了避免死循环，设置循环次数来定义超时时长
            int maxCount = bigModelConfig.getMaxResponseTime() * 5;
            while (count <= maxCount) {
                Thread.sleep(200);
                if (listener.isWsCloseFlag()) {
                    break;
                }
                count++;
            }
            if (count > maxCount) {
                return "大模型响应超时，请联系管理员";
            }
            // 响应大模型的答案
            return listener.getAnswer().toString();
        } catch (InterruptedException e) {
            log.error("错误：" + e.getMessage());
            return "系统内部错误，请联系管理员";
        } finally {
            // 关闭 websocket 连接
            webSocket.close(1000, "");
            // 归还令牌
            bigModelStreamClient.operateToken(BigModelStreamClient.BACK_TOKEN_STATUS);
        }
    }

    /**
     * 清洗大模型返回内容
     */
    public String cleanAiResponse(String rawResponse) {
        // 去除Markdown代码块标识
        String cleaned = rawResponse.replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        return cleaned;
    }
}
```

####  7)调用大语言模型

##### i)constants包

先将相关参数提取到常量类中，提高代码的可维护性

```java
/**
 * 枚举、常量定义
 */
public class ConfigConstants {
    /**
     * 响应码相关
     */
    // 成功响应码
    public static final int SUCCESS = 20000;
    // 失败响应码
    public static final int FAIL = 20001;

    /**
     * 文字识别相关
     */
    // 使用百度OCR服务时的Token
    public static final String OCR_TOKEN = "";
    // 调用百度OCR服务的api
    public static final String OCR_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";

    /**
     * 大模型相关
     */
    public static final String HOST_URL = "https://spark-api.xf-yun.com/v1.1/chat";
    public static final String DOMAIN = "lite";
    public static final String APP_ID = "";
    public static final String API_SECRET = "";
    public static final String API_KEY = "";
    // 用于获得图书信息的问题
    public static final String SCAN_BOOK_QUESTION = "请根据此信息,给出图书标题、作者、ISBN号、简介，并以这样的{ title: 'Book Name', author: 'Author Name', isbn: 'ISBN Number', introduction: 'Book Introduction'}json串回答";
}
```

##### ii)service包

对前文的service进行了修改，因为前文主要是进行测试。

```java
public interface IBookService extends IService<Book> {
    /**
     * 识别图书
     * @param file_base64
     * @return 文字识别获取到的信息
     *         格式如下:
     *          {"words_result":[
     *               {"words":"活着/余华著.-3版.-北京：作家出版社，2012.8"},
     *               {"words":"(2018.8重印)"},{"words":"(余华作品)"},
     *               {"words":"ISBN978-7-5063-6543-7"}],
     *           "words_result_num":4,
     *           "log_id":1889514639947915854
     *			}
     */
    String ocr(String file_base64);
}
```

##### iii)service_impl包

对前文的service_impl进行了修改，因为前文主要是进行测试。

```java
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    /**
     * 文字识别图书
     * @return
     */
    @Override
    public String ocr(String file_base64) {
        // 请求url
        String url = OCR_URL;

        try {
            // 将base64编码进行url encode
            String imgParam = URLEncoder.encode(file_base64, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = OCR_TOKEN;

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println("result" + result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

##### iv)controller包

对前文的controller进行了修改，因为前文主要是进行测试。

```java
@RestController
@RequestMapping("/book")
public class BooksController {
    @Autowired
    private IBookService bookService;

    @Autowired
    private BigModelUtil bigModelUtil;

    @PostMapping("/ocr")
    public Result<Map<String,Object>> ocr(@RequestBody MultipartFile file) {
        // 将MultipartFile转成Base64
        String file_base64 = ImageToBase64.generateBase64(file);

        // 调用百度OCR服务 ocr_result:识别到的文字
        String ocr_result = bookService.ocr(file_base64);

        // question:将ocr_result拼接上询问大语言模型的问题
        String question = ocr_result + SCAN_BOOK_QUESTION;

        /* 调用大语言模型得到answer
            {
                "title": "活着",
                "author": "余华",
                "isbn": "978-7-5063-6543-7",
                "introduction": "《活着》是余华的作品，首次出版于2012年。这本书是一部长篇小说，讲述了主人公福贵一生的坎坷经历和对生活的深刻感悟。"
            }
         */
        String answer = bigModelUtil.sendQuestion(question);
        // 打印answer，方便检查问题
        System.out.println("answer:" + answer);

        // 由于大语言模型返回的json串为Markdown格式，需要对answer进行去除多余的内容得到json串(string)
        answer = bigModelUtil.cleanAiResponse(answer);

        // 将answer转为Map得到data
        JSONObject jsonObject = JSON.parseObject(answer);
        Map<String, Object> data = (Map<String, Object>) jsonObject;
        // 打印data，方便检查问题
        System.out.println(data);

        if (data != null) {
            return Result.success(data, "识别成功!!!");
        }
        return Result.fail("识别错误!!!");
    }
}
```

#### 8)录入图书

| 接口属性 | 值                                                           |
| :------- | ------------------------------------------------------------ |
| url      | /book/add                                                    |
| method   | post                                                         |
| 请求参数 | bookData                                                     |
| 返回参数 | ![](https://pic1.imgdb.cn/item/67b03967d0e0a243d4ff8fe6.png) |

##### i)service

```java
public interface IBookService extends IService<Book> {
    /**
     * 录入图书
     * @param book
     * @return 是否成功录入
     */
    boolean addBook(Book book);
}
```

##### ii)service_impl

```java
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {
    /**
     * 图书录入
     */
    public boolean addBook(Book book) {
        QueryWrapper<Book> bookWrapper = new QueryWrapper<>();
        // 检查图书是否存在(ISBN为主键)
        bookWrapper.eq("isbn", book.getIsbn());
        Book select_book = baseMapper.selectOne(bookWrapper);

        // 便于检查
        System.out.println(select_book);

        // 判断图书是否存在
        if (select_book == null) {
            boolean isSave = save(book);
            System.out.println(isSave);
            return isSave;
        }
        // 图书已经存在
        return false;
    }
}
```

##### iii)controller

```java
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private IBookService bookService;

    @Autowired
    private BigModelUtil bigModelUtil;

    // 录入图书
    @PostMapping("/add")
    public Result<Map<String,Object>> addBook(@RequestBody Book book) {
        // 打印book，方便检查问题
        System.out.println(book);

        // 录入图书
        boolean isSave = bookService.addBook(book);

        if (isSave) {
            return Result.success("录入成功!!!");
        } else {
            return Result.fail("该图书已存在");
        }
    }
}
```

#### 9)改进

在前面的基础上，添加获取图书封面以及图书评论报告的功能。

##### i)获取图书封面

利用豆瓣api

参考:[豆瓣开发者不完全指南 - Mukti](https://feizhaojun.com/?p=3813)

###### a)config

```java
/**
 * 豆瓣相关
 */
public static final String DB_API_KEY = "0ac44ae016490db2204ce0a042db2916";
public static final String DB_URL = "https://api.douban.com/v2/book/isbn";
```

###### b)service

```java
public interface IBookService extends IService<Book> {
    /**
     * 获取图书封面
     * @param isbn
     * @return 图书封面
     */
    String getImage(String isbn);
}
```

###### c)serviceImpl

```java
/**
 * 获取图书封面
 */
public String getImage(String isbn) {
    RestTemplate restTemplate = new RestTemplate();

    // 请求url
    String url = DB_URL + "/" + isbn + "?apikey=" + DB_API_KEY;
    String response = restTemplate.getForObject(url, String.class);
    String image = JSON.parseObject(response).getString("image");

    System.out.println("image:" + image);

    return image;
}
```

##### ii)获取图书评论报告

###### a)config

```java
// 用于获得评论转json的问题
    public static final String GET_COMMENTS_JSON_QUESTION = "{\n" +
            "  \"positive\": [\n" +
            "    {\n" +
            "      \"title\": \"人物塑造深刻\",\n" +
            "      \"content\": \"《活着》的人物塑造非常成功，特别是主人公福贵的命运波折和内心世界的变化，都让人深感共鸣。余华通过细腻的笔触描绘了人性的复杂性和生命的脆弱。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"主题深远\",\n" +
            "      \"content\": \"小说的主题深邃，探讨了生命的意义、苦难与希望的关系，以及人在面对困境时的选择和坚持。余华以其独特的视角，让我们对生活有了更深的思考。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"文笔精湛\",\n" +
            "      \"content\": \"余华的文笔非常出色，他能够用简洁而富有力量的语言，将复杂的情感和深刻的思考表达得淋漓尽致。阅读《活着》的过程，就是一种享受。\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"negative\": [\n" +
            "    {\n" +
            "      \"title\": \"语言晦涩难懂\",\n" +
            "      \"content\": \"虽然余华的文笔很有特色，但有些部分的叙述可能对于非专业读者来说稍显晦涩，不易理解。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"情节发展缓慢\",\n" +
            "      \"content\": \"小说中的情节发展相对较慢，一些情节转折可能让读者感到不够流畅，影响了阅读的连贯性。\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"comprehensive\": [\n" +
            "    {\n" +
            "      \"title\": \"文学价值\",\n" +
            "      \"content\": \"《活着》作为余华的代表作之一，不仅在文学上具有很高的成就，其深刻的主题和精湛的文笔也使其成为一部值得深入阅读的经典之作。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"历史价值\",\n" +
            "      \"content\": \"小说通过对一个普通人的一生经历的描写，反映了社会变迁下个体的生活状态和心理变化，为研究中国近现代史提供了宝贵的第一手资料。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"社会影响\",\n" +
            "      \"content\": \"《活着》自发布以来，受到了广泛的关注和好评，它的影响力远远超出了文学领域，成为了中国现当代文学的一个重要标志。\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"rating\": \"8.5/10\",\n" +
            "  \"recommendation\": \"强烈推荐给对中国现当代文学有兴趣的读者，尤其是喜欢余华作品的读者。\"\n" +
            "}请你按照这个形式，根据以下信息，爬取豆瓣网的相关评论，得到评论报告，按照上面的形式返回json";
```

##### iii)代码

controller

```java
// 录入图书
@PostMapping("/add")
public Result<Map<String,Object>> addBook(@RequestBody Book book) {
    // 打印book，方便检查问题
    System.out.println(book);

    // ---------------- 新增部分start ----------------
    // 获取图书封面
    String image = bookService.getImage(book.getIsbn());
    book.setImage(image);

    // 获取图书评论报告
    String book_data = "{title:" + book.getTitle() + ",author:" +book.getAuthor() + ",isbn:" +book.getIsbn() + ",introduction:" + book.getIntroduction() + "}";
    String question = GET_COMMENTS_JSON_QUESTION + book_data;
    String comments = bigModelUtil.sendQuestion(question);
    // 由于大语言模型返回的json串为Markdown格式，需要对answer进行去除多余的内容得到json串(string)
    comments = bigModelUtil.cleanAiResponse(comments);
    book.setComments(comments);

    // 方便检查
    System.out.println("comments:" + comments);
    // ---------------- 新增部分end ----------------

    // 录入图书
    boolean isSave = bookService.addBook(book);

    if (isSave) {
        return Result.success("录入成功!!!");
    } else {
        return Result.fail("该图书已存在");
    }
}
```

### 5.功能二：图书管理

#### 1)查询图书

| 接口属性  | 值                                                           |
| :-------- | ------------------------------------------------------------ |
| url       | /book/list                                                   |
| methodget | get                                                          |
| 请求参数  | title=xx&isbn=xx&autho=xx&pageNo=xx&pageSize=xx              |
| 返回参数  | ![](https://pic1.imgdb.cn/item/67b14b10d0e0a243d4ffba95.png) |

##### i)controller

```java
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private IBookService bookService;

    @Autowired
    private BigModelUtil bigModelUtil;

    @GetMapping("/list")
    public Result<Map<String, Object>> getBookList(@RequestParam(value = "title", required = false) String title,
                                                  @RequestParam(value = "isbn", required = false) String isbn,
                                                  @RequestParam(value = "author", required = false) String author,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(title), Book::getTitle, title);
        wrapper.eq(StringUtils.hasLength(isbn), Book::getIsbn, isbn);
        wrapper.eq(StringUtils.hasLength(author), Book::getAuthor, author);

        Page<Book> page = new Page<>(pageNo, pageSize);
        bookService.page(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        return Result.success(data);
    }
}
```

##### ii)config

配置分页插件，在config包下新建MybatisPlusConfig

```java
@Configuration
public class MybatisPlusConfig {
    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

##### iii)测试

如果没有配置分页插件的话，total会为0。

![](https://pic1.imgdb.cn/item/67b14b10d0e0a243d4ffba95.png)

#### 2)修改图书

| 接口属性  | 值                                                           |
| :-------- | ------------------------------------------------------------ |
| url       | /book                                                        |
| methodget | put                                                          |
| 请求参数  | book                                                         |
| 返回参数  | ![](https://pic1.imgdb.cn/item/67b1def1d0e0a243d4ffdd3f.png) |

##### i)controller

```java
// 修改图书
@PutMapping
public Result<?> updateBook(@RequestBody Book book) {
    bookService.updateById(book);
    return Result.success("修改图书信息成功!!");
}
```

#### 3)根据ISBN查询图书

| 接口属性  | 值                                                           |
| :-------- | ------------------------------------------------------------ |
| url       | /book                                                        |
| methodget | get                                                          |
| 请求参数  | isbn                                                         |
| 返回参数  | ![](https://pic1.imgdb.cn/item/67b1e023d0e0a243d4ffdd59.png) |

##### i)controller

```java
// 根据ISBN查询图书
@GetMapping("/{isbn}")
public Result<Book> getBookByIsbn(@PathVariable("isbn") String isbn) {
    Book book = bookService.getById(isbn);
    return Result.success(book);
}
```

### 6.功能三：统计分析

| 接口属性  | 值                                                           |
| :-------- | ------------------------------------------------------------ |
| url       | /statistic/data                                              |
| methodget | get                                                          |
| 请求参数  |                                                              |
| 返回参数  | ![](https://pic1.imgdb.cn/item/67b7ecacd0e0a243d40131e1.png) |

#### 1)service

```java
public interface IBookService extends IService<Book> {
    /**
     * 获取总销量
     * @return 总销量
     */
    public long getSaleTotal();

    /**
     * 获得热销前6的图书
     * @return 热销前6的图书
     */
    public List<Book> findTop6BySale();

    /**
     * 根据isbn获取图书相关信息
     * @param isbn
     * @return 图书相关信息Json String形式
     */
    public String getBookMessage(String isbn);
}
```

#### 2)service_impl



```java
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {
    @Autowired
    private BookMapper bookMapper;

    /**
     * 获得总销量
     */
    public long getSaleTotal() {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("SUM(sale) as saleTotal");
        Map<String, Object> result = bookMapper.selectMaps(queryWrapper).get(0);
        BigDecimal saleTotal = (BigDecimal) result.get("saleTotal");
        return saleTotal.longValue();
    }

    /**
     * 获得热销前6的图书
     */
    public List<Book> findTop6BySale() {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sale"); // 按销量降序排序
        queryWrapper.last("LIMIT 6"); // 限制结果为前6条记录
        return bookMapper.selectList(queryWrapper);
    }

    /**
     * 获得图书相关信息
     */
    public String getBookMessage(String isbn) {
        RestTemplate restTemplate = new RestTemplate();

        // 请求url
        String url = DB_URL + "/" + isbn + "?apikey=" + DB_API_KEY;

        return restTemplate.getForObject(url, String.class);
    }
}
```

#### 3)controller

```java
@RestController
@RequestMapping(value = {"/book", "/statistic"})
public class BookController {
    @Autowired
    private IBookService bookService;

    @Autowired
    private BigModelUtil bigModelUtil;

    // 获得统计数据
    @GetMapping("/data")
    public Result<?> getStatisticData() {
        // 总销量
        long saleTotal = bookService.getSaleTotal();
        // 图书数
        long bookTotal = bookService.count();
        // 热销前6的图书
        List<Book> top6Books = bookService.findTop6BySale();
        List<Map<String, Object>> hotBooks = new ArrayList<>();
        // 词云图数据
        List<Map<String, Object>> keywordData = new ArrayList<>();

        // 填充hotBooks及keywordData
        int rank = 1;
        for (Book book : top6Books) {
            // 获得相应图书信息
            String response = bookService.getBookMessage(book.getIsbn());
            JSONObject jsonObject = JSON.parseObject(response);
            // 图书评分
            String rating = jsonObject.getJSONObject("rating").getString("average");
            // 图书tags
            JSONArray tags = jsonObject.getJSONArray("tags");

            // 填充hotBooks
            Map<String, Object> bookMap = new HashMap<>();
            bookMap.put("rank", rank);
            bookMap.put("title", book.getTitle());
            bookMap.put("author", book.getAuthor());
            bookMap.put("sales", book.getSale());
            bookMap.put("rating", rating);
            hotBooks.add(bookMap);
            rank++;

            // 填充keywordData
            for(Object tagObject : tags) {
                JSONObject tag = (JSONObject) tagObject;
                Map<String, Object> keywordMap = new HashMap<>();
                keywordMap.put("name", tag.getString("name"));
                keywordMap.put("value", tag.getString("count"));
                keywordData.add(keywordMap);
            }
        }

        // 将数据都放入data中，然后传给前端
        Map<String, Object> data = new HashMap<>();
        data.put("saleTotal", saleTotal);
        data.put("bookTotal", bookTotal);
        data.put("hotBooks", hotBooks);
        data.put("keywordData", keywordData);

        // 便于检查
        System.out.println(data);
        return Result.success(data);
    }
}
```



## 前后端对接

### 1.修改相应配置

![](https://pic1.imgdb.cn/item/67aacd5bd0e0a243d4fe4180.png)

### 2.解决跨域问题

#### 1）跨域问题

![](https://pic1.imgdb.cn/item/67ab0470d0e0a243d4fe509e.png)

#### 2）解决

##### i)添加注解

在相应控制器上添加

```java
@CrossOrigin
```

##### ii)编写配置类

```java
@Configuration
public class MyCorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //1) 允许的域,不要写*，否则cookie就无法使用了
        config.addAllowedOrigin("http://localhost:8888"); //这里填写请求的前端服务器
        //2) 是否发送Cookie信息
        config.setAllowCredentials(true);
        //3) 允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        // 4）允许的头信息
        config.addAllowedHeader("*");

        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}
```



## 数据库

### 1.数据库初始化

```sql
CREATE DATABASE book_db

USE book_db;

CREATE TABLE Book (
    isbn VARCHAR(13) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    introduction TEXT,
    comments TEXT,
    image varchar(200),
    stock INT DEFAULT 0,
    sale INT,
    deleted INT(1) DEFAULT 0
);
```

























## 问题

### 1.Github.com网站打开缓慢问题

在<u>C:\Windows\System32\drivers\etc</u>中的hosts中加入以下内容

![](https://pic1.imgdb.cn/item/678e0a31d0e0a243d4f5dfd2.png)

参考：[GitHub打不开的解决方案（超简单）-CSDN博客](https://blog.csdn.net/weixin_43804496/article/details/131475204)

### 2.VS Code Git提交代码卡顿问题

参考：[解决Vscode使用git提交卡住的问题_vscode 提交代码很慢-CSDN博客](https://blog.csdn.net/qq_42632840/article/details/134581517#:~:text=使用 Vscode 的 git 提交代码经常会很慢%2F卡住。 找到git的配置 (建议直接搜索)，把use Editor,信息这一关键步骤，以避免 过程卡顿。 本文不仅解析了这一 BUG 的成因，还提供了 方案和操作建议。 希望本文能帮助你高效地在 中，提升开发效率。)

### 3.pom文件报错问题

![](https://pic1.imgdb.cn/item/67930de0d0e0a243d4f76aca.png)

参考：[首次使用IDEA创建maven项目出现“Dependency ‘org.springframework.boot:spring-boot-starter-web:‘ not found”问题_dependency 'org.springframework.boot:spring-boot-s-CSDN博客](https://blog.csdn.net/qq_37970469/article/details/116905582)

### 4.IDEA报错”cannot resolve symbol xxx“问题

此处注意对应修改

![](https://pic1.imgdb.cn/item/6795a7c1d0e0a243d4f802a7.png)

### 5.前后端对接由于没有实现登录逻辑解决

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public Result<Map<String, Object>> login() {
        Map<String, Object> data = new HashMap<>();
        data.put("token", "admin-token");
        return Result.success(data);
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getInfo() {
        Map<String, Object> data = new HashMap<>();
        data.put("roles", "['admin']");
        data.put("introduction", "I am a super administrator");
        data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        data.put("name", "Super Admin");
        return Result.success(data);
    }

    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        return Result.success("注销成功");
    }
}
```

参考：[SpringBoot整合vue-admin-template实现登录_vue-admin-template官网-CSDN博客](https://blog.csdn.net/liu320yj/article/details/109337089)

### 6.后端正确返回了数据，但前端并没有按要求填充表单

此处应为response.data.data

***在代码中直接使用response.data获取的是整个响应对象，包括code、message和data。因此，应该访问response.data.data来获取实际的图书信息。***

![](https://pic1.imgdb.cn/item/67af1020d0e0a243d4ff2e9c.png)

### 7.使用Mybatis-plus的selectById方法时，由于数据库主键名不为id报错问题

在实体类的主键上添加注解@TableId(value = "isbn", type = IdType.INPUT)

```java
@TableId(value = "isbn", type = IdType.INPUT)
private String isbn;
```

### 8.图片加载不出来

在public/index.html中加入

```html
<meta name="referrer" content="never">
```

###  9.文件上传报错FileSizeLimitExceededException

在application.yml中加入

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 5MB
```

### 10.录入图书时，由于修改了后端逻辑，处理时间较长，前端会导致超时问题

修改src/utils/request.js，将timeout从5000修改成15000

```js
// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 15000 // request timeout
})
```

