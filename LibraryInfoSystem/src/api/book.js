import { title } from '@/settings'
import request from '@/utils/request'

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
