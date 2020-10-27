import axios from 'axios'
import domain from '@/../public/domain'
import {MessageBox} from 'element-ui'

axios.defaults.baseURL = domain.baseUrl // 配置axios请求的地址
axios.defaults.crossDomain = true;
axios.defaults.withCredentials = true;  //设置cross跨域 并设置访问权限 允许跨域携带cookie信息

let baseUrl = 'http://localhost'
export const getDeleteOne = params => {
    return axios.get(`${baseUrl}/user/DeleteOne`, {params: params})
}
axios.interceptors.response.use(
    response => {
        return response;
    },
    // 服务器状态码不是200的情况
    error => {
        if (error.response.status) {
            switch (error.response.status) {
                // 401: 未登录
                // 未登录则跳转登录页面，并携带当前页面的路径
                // 在登录成功后返回当前页面，这一步需要在登录页操作。
                case 401: {
                    MessageBox('您还未登录，即将跳转到登录界面', '提示', {
                        confirmButtonText: '确定',
                    }).then(() => {
                            location.replace('/#/login');
                        }
                    )
                    break;
                }
                default:
            }
            return Promise.reject(error);
        }
    }
);

export default axios;
