import { createBrowserRouter } from 'react-router-dom';
import Home from '../components/novels/Home';
import { novelRouter } from './novelRouter';
import { userRouter } from './userRouter';
import UserPage from '../pages/users/UserPage';

// http://localhost:5173/ => Home
// http://localhost:5173/novels/add => Add
// http://localhost:5173/novels/edit/1 => Edit
// http://localhost:5173/novels/1 => Detail

const rootRouter = createBrowserRouter([
  {
    path: '/',
    Component: Home,
  },
  {
    path: '/novels',
    children: novelRouter(),
  },
  {
    path: '/member',
    Component: UserPage,
    children: userRouter(),
  },
]);

export default rootRouter;
