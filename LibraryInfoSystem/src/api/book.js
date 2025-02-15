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
        url: 'book/add',
        method: 'post',
        data
    })
}