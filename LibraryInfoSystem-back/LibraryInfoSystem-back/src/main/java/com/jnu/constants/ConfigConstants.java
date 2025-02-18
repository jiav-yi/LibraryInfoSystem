package com.jnu.constants;

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
    public static final String OCR_TOKEN = "24.73e9c81fa2fb1a4dc3ab5a20b2306349.2592000.1741601729.282335-117382515";
    // 调用百度OCR服务的api
    public static final String OCR_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";

    /**
     * 大模型相关
     */
    public static final String HOST_URL = "https://spark-api.xf-yun.com/v1.1/chat";
    public static final String DOMAIN = "lite";
    public static final String APP_ID = "6870d580";
    public static final String API_SECRET = "MThlMzYxMTM0Njg5ZTM5OTU0NWU2NmUy";
    public static final String API_KEY = "cca95262288af7daa8e3278a4dc64a78";
    // 用于获得图书信息的问题
    public static final String SCAN_BOOK_QUESTION = "请根据此信息,给出图书标题、作者、ISBN号、简介，并以这样的{ title: 'Book Name', author: 'Author Name', isbn: 'ISBN Number', introduction: 'Book Introduction'}json串回答";
    // 用于获得图书评论报告的问题
    public static final String GET_COMMENTS_QUESTION = "1. 正面评价\n" +
            "人物塑造成功：《三国演义》通过生动的描写，成功塑造了一系列鲜明的人物形象。例如，刘备的仁德、关羽的义气、张飞的勇猛、诸葛亮的智慧等，都给读者留下了深刻的印象。这些人物形象不仅具有鲜明的个性特点，还体现了中国传统文化中的仁、义、礼、智、信等核心价值观。\n" +
            "情节紧凑：小说以宏大的结构，将百年左右的历史事件和众多的人物组织得完整严密，叙述得有条不紊、前后呼应。通过一系列精彩的战争描写和政治斗争，展现了历史的变迁和人物的命运。\n" +
            "历史与文学的结合：《三国演义》在创作上继承了传统史学的实录精神，以“七实三虚”为原则，将历史事件和文学创作有机结合。小说中虽有不少虚构成分，但大的历史事件皆取之于史册，主要人物的性格和经历也基本符合史实。\n" +
            "2. 负面评价\n" +
            "历史与文学的混淆：《三国演义》在创作过程中，有时会将历史和文学的界限混淆，导致读者难以区分哪些是历史事实，哪些是文学加工。这种混淆可能会让读者对历史事件和人物产生误解。\n" +
            "人物形象的片面性：部分读者认为，小说中对某些人物的描写过于片面，例如曹操被描绘为“奸雄”，而刘备则被描绘为“仁君”，这种对立的描写可能会让读者对历史人物产生偏见。\n" +
            "3. 总体评价\n" +
            "文学价值：《三国演义》作为中国古典文学的四大名著之一，具有极高的文学价值。它不仅在人物塑造和情节设计上表现出色，还在军事、政治、外交等方面提供了丰富的描写，展现了历史的复杂性和人物的智慧。\n" +
            "历史价值：小说通过生动的描写，反映了东汉末年的社会风貌和历史变迁，为后人提供了宝贵的历史资料和启示。\n" +
            "社会影响：《三国演义》的社会影响远远超过了它的文学价值，它不仅是中国文学史上的经典之作，还对后世的文学创作和历史研究产生了深远的影响。\n" +
            "4. 评分\n" +
            "综合评分：9.0/10\n" +
            "读者推荐：强烈推荐给对中国历史和古典文学感兴趣的读者。" + "请按照这个形式，结合以下的信息，爬取豆瓣网的相关评论，返回评论报告(只需要评论报告)";
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

    /**
     * 豆瓣相关
     */
    public static final String DB_API_KEY = "0ac44ae016490db2204ce0a042db2916";
    public static final String DB_URL = "https://api.douban.com/v2/book/isbn";
}
