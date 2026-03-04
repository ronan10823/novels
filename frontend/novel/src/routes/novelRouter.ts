import AddNovel from '../pages/novels/AddNovel';
import EditNovel from '../pages/novels/EditNovel';
import NovelDetails from '../pages/novels/NovelDetails';

export const novelRouter = () => {
  return [
    {
      path: 'add',
      Component: AddNovel,
    },
    {
      path: 'edit/:id',
      Component: EditNovel,
    },
    {
      path: ':id',
      Component: NovelDetails,
    },
  ];
};
