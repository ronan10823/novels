import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { postRegister } from '../../apis/userApis';

function RegisterForm() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    email: '',
    password: '',
    nickname: '',
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await postRegister(form);
      alert('회원가입 성공');
      navigate('/user/login');
    } catch (err: any) {
      alert(err.response?.data?.message || '회원가입 실패');
    }
  };

  return (
    <form className="mt-6 flex flex-col gap-3" onSubmit={handleSubmit}>
      <input
        name="email"
        placeholder="id"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        onChange={handleChange}
      />
      <input
        type="password"
        name="password"
        placeholder="password"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        onChange={handleChange}
      />
      <input
        type="text"
        name="nickname"
        placeholder="nickname"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        onChange={handleChange}
      />
      <div className="p-2 text-center">
        <button
          type="submit"
          className="mx-1 my-6 rounded-[3px] bg-red-700 px-4.5 py-3 text-[1.2em] text-white hover:bg-red-900"
        >
          회원가입
        </button>
      </div>
    </form>
  );
}

export default RegisterForm;
