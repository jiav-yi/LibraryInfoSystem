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