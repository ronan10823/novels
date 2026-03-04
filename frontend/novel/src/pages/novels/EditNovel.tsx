import { useNavigate, useParams } from 'react-router-dom';
import NovelForm from '../../components/novels/NovelForm';
import BasicLayout from '../../layouts/BasicLayout';
import { useNovel } from '../../hooks/useNovel';
import Error from '../../components/common/Error';
import Loading from '../../components/common/Loading';
import type { Novel } from '../../types/book';
import { putNovel } from '../../apis/novelApis';
import useLogin from '../../hooks/useLogin';

const EditNovel = () => {
  // http://localhost:5173/novels/edit/119
  // id 가져오기
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isLogin } = useLogin();

  // 서버로 novel 요청
  const { serverData, loading, error } = useNovel(id);

  const handleCancel = (id: number) => {
    // 이전 페이지로 이동
    navigate(`../${id}`);
  };

  const handleSubmit = async (formData: Novel) => {
    // 서버로 업데이트 요청
    try {
      const result = await putNovel(formData);
      console.log('수정 후 ', result);
      // 상세보기
      navigate(`../${id}`);
    } catch (error) {
      console.log(error);
    }
  };

  // 로그인 여부 확인?
  // 로그인 페이지 이동
  if (!isLogin) navigate('/member/login');

  if (error) return <Error />;

  return (
    <BasicLayout>
      <h1 className="text-[32px]">Edit Book</h1>
      {loading ? (
        <Loading />
      ) : (
        <NovelForm
          novel={serverData}
          onCancel={handleCancel}
          onSubmit={handleSubmit}
        />
      )}
    </BasicLayout>
  );
};

export default EditNovel;
