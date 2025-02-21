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