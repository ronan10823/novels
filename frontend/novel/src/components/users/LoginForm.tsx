import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useLogin from '../../hooks/useLogin';

const initState = {
  email: '',
  pw: '',
};

function LoginForm() {
  const navigate = useNavigate();

  const { doLogin, moveToPath } = useLogin();

  const [loginParam, setLoginParam] = useState(initState);
  const { email, pw } = loginParam;

  const handleSubmit = async (e: React.SubmitEvent) => {
    e.preventDefault();
    // console.log(loginParam);

    try {
      const data = await doLogin(loginParam);

      if (data?.accessToken) {
        alert('로그인 성공');
        moveToPath('/');
      }
    } catch (error) {
      alert('이메일과 비밀번호를 확인해 주세요');
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setLoginParam({
      ...loginParam,
      [name]: value,
    });
  };

  return (
    <form className="mt-6 flex flex-col gap-3" onSubmit={handleSubmit}>
      <input
        name="email"
        placeholder="email"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        onChange={handleChange}
        value={email}
      />
      <input
        type="password"
        name="pw"
        placeholder="password"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        onChange={handleChange}
        value={pw}
      />
      <div className="p-2 text-center">
        <button
          type="submit"
          className="mx-1 my-6 rounded-[3px] bg-sky-500 px-4.5 py-3 text-[1.2em] text-white hover:bg-sky-800"
        >
          로그인
        </button>
        <button
          onClick={() => navigate('../register')}
          type="button"
          className="mx-1 my-6 rounded-[3px] bg-red-700 px-4.5 py-3 text-[1.2em] text-white hover:bg-red-900"
        >
          회원가입
        </button>
      </div>
    </form>
  );
}

export default LoginForm;
