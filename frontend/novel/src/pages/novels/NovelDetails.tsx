import { useParams } from 'react-router-dom';
import NovelDetail from '../../components/novels/NovelDetail';
import { useNovel } from '../../hooks/useNovel';
import BasicLayout from '../../layouts/BasicLayout';
import Loading from '../../components/common/Loading';
import Error from '../../components/common/Error';

const NovelDetails = () => {
  // http://localhost:5173/novels/89
  const { id } = useParams<{ id: string }>();
  const { serverData, loading, error } = useNovel(id);

  if (error) return <Error />;

  return (
    <BasicLayout>
      <h1 className="text-[32px]">Book Details</h1>
      {loading ? <Loading /> : <NovelDetail novel={serverData} />}
    </BasicLayout>
  );
};

export default NovelDetails;
