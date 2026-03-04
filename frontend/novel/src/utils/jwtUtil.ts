import type {
  AxiosError,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from 'axios';
import axios from 'axios';
import { getCookie, setCookie } from './cookieUtil';

const jwtAxios = axios.create();

const host = 'http://localhost:8080';
const refreshJWT = async (accessToken: string, refreshToken: string) => {
  // 기존의 토큰을 refresh(사용자의 토큰을 사용자에게 노출 없이 토큰 재발급)
  const header = { headers: { Authorization: `Bearer ${accessToken}` } };
  const res = await axios.get(
    `${host}/api/member/refresh?refreshToken=${refreshToken}`,
    header,
  );
  return res.data;
};

const requestFail = (err: AxiosError) => {
  console.log('request error');
  return Promise.reject(err);
};
const responseFail = (err: AxiosError) => {
  console.log('response error');
  return Promise.reject(err);
};

const beforeReq = (config: InternalAxiosRequestConfig) => {
  console.log('before Request');

  const member = getCookie('member');
  if (!member) {
    return Promise.reject({
      response: {
        data: { error: '실패' },
      },
    });
  }

  // 쿠키가 있다면 쿠키에서 accessToken 만 가져오기
  const { accessToken } = member;
  config.headers.Authorization = `Bearer ${accessToken}`;

  return config;
};

const beforeRes = async (
  res: AxiosResponse<unknown>,
): Promise<AxiosResponse<unknown>> => {
  console.log('before return response...');

  const data = res.data as { error?: string };

  if (data?.error == 'ERROR_ACCESS_TOKEN') {
    // jwtcheckfilter(backend)의 try-catch 구문에 정의해둔 error 구문 적용
    const memberCookieVal = getCookie('member');

    const result = await refreshJWT(
      memberCookieVal.accessToken,
      memberCookieVal.refreshToken,
    );
    memberCookieVal.accessToken = result.accessToken;
    memberCookieVal.refreshToken = result.refreshToken;
    setCookie('member', JSON.stringify(memberCookieVal), 1);

    const originRequest = res.config;
    originRequest.headers.Authorization = `Bearer ${result.accessToken}`;

    return await axios(originRequest);
  }
  return res;
};

jwtAxios.interceptors.request.use(beforeReq, requestFail);
jwtAxios.interceptors.response.use(beforeRes, responseFail);

export default jwtAxios;
