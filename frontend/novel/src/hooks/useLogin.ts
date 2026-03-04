import { useDispatch, useSelector } from 'react-redux';
import type { AppDispatch, RootState } from '../reducers/store';
import { loginPostAsync, logout } from '../reducers/loginSlice';
import type { LoginForm } from '../types/user';
import { useNavigate } from 'react-router-dom';

const useLogin = () => {
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();

  // store에 있는 상태값 가져오기
  const authState = useSelector((state: RootState) => state.auth);

  // 로그인 여부
  const isLogin = authState.email ? true : false;

  // 로그인 함수
  const doLogin = async (loginParam: LoginForm) => {
    const data = await dispatch(loginPostAsync(loginParam)).unwrap();
    return data;
  };

  // 로그아웃 함수
  const doLogout = () => {
    dispatch(logout());
  };

  // 로그인 후 경로 이동
  const moveToPath = (path: string) => {
    navigate({ pathname: path }, { replace: true });
  };

  const movetoLogin = () => {
    navigate({ pathname: '/member/login' }, { replace: true });
  };

  return { authState, isLogin, doLogin, doLogout, moveToPath, movetoLogin };
};

export default useLogin;
