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

          <!-- 评论部分 -->
          <div class="book-comments">
            <h3>评论</h3>
            <div v-if="formattedComments" v-html="formattedComments"></div>
            <div v-else>暂无评论</div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
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
    }
  },
  created() {
    const bookData = this.$route.query.book;
    if (bookData) {
      this.book = JSON.parse(bookData);
    }
  },
  methods: {

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
</style>