// 서버 연동 담당

import axios from 'axios';
import type { LoginForm, RegisterForm } from '../types/user';

export const API_SERVER_HOST = '/api/member';

// 로그인 post
export const postLogin = async (loginParam: LoginForm) => {
  const form = new FormData();
  // login 시 id는 username, 비밀번호는 password
  form.append('username', loginParam.email);
  form.append('password', loginParam.pw);

  const res = await axios.post(`${API_SERVER_HOST}/login`, form, {
    headers: { 'Content-Type': 'x-www-form-urlencoded' },
  });
  console.log('서버 도착 ', res);
  return res.data;
};

// 회원가입 post
export const postRegister = async (registerParam: RegisterForm) => {
  const res = await axios.post(`${API_SERVER_HOST}/register`, registerParam);
  return res.data;
};
