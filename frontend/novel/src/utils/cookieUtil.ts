// 로그인 정보 쿠키에 저장하거나 제거하는 함수 정의

import { Cookies } from 'react-cookie';

const cookies = new Cookies();

// cookie 세팅
export const setCookie = (name: string, value: string, days: number) => {
  const expires = new Date();

  expires.setUTCDate(expires.getUTCDate() + days);
  return cookies.set(name, value, { path: '/', expires: expires });
};
export const getCookie = (name: string) => {
  return cookies.get(name);
};
export const removeCookie = (name: string, path = '/') => {
  cookies.remove(name, { path });
};
