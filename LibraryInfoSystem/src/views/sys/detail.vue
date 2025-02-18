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