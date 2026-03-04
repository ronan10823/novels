import LoginForm from '../components/users/LoginForm';
import RegisterForm from '../components/users/RegisterForm';

export const userRouter = () => {
  return [
    {
      path: 'login',
      Component: LoginForm,
    },
    {
      path: 'register',
      Component: RegisterForm,
    },
  ];
};
