// api/axios.js
import axios from 'axios';

const instance = axios.create({
	baseURL: ' http://localhost:8083', // 设置你的API基础URL
	timeout: 5000, // 请求超时时间
});

// 添加请求拦截器
instance.interceptors.request.use(
	(config) => {
		// 在请求发送之前可以进行一些处理，例如添加token等
		return config;
	},
	(error) => {
		// 处理请求错误
		return Promise.reject(error);
	}
);

// 添加响应拦截器
instance.interceptors.response.use(
	(response) => {
		// 对响应数据进行处理
		return response.data;
	},
	(error) => {
		// 处理响应错误
		return Promise.reject(error);
	}
);

export default instance;
