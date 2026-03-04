import React from 'react';
import { useSearchParams } from 'react-router-dom';
import { useNovels } from '../../hooks/useNovels';
import BasicLayout from '../../layouts/BasicLayout';
import { genres } from '../../utils/novelUtil';
import Error from '../common/Error';
import Loading from '../common/Loading';
import NovelList from './NovelList';

const Home = () => {
  // 파라메터 값 가져오기
  // http://localhost:5173/?page=1&genre=1&keyword=The Hobbit
  const [searchParams, setSearchParams] = useSearchParams();

  //페이지 나누기 현재 페이지 저장용
  // const [currentPage, setCurrentPage] = useState(0);

  const genre = Number(searchParams.get('genre') ?? 0);
  const keyword = searchParams.get('keyword') ?? '';
  const currentPage = Number(searchParams.get('page') ?? 0);

  const { serverData, loading, error, toggleAvailable } = useNovels(
    currentPage,
    10,
    genre,
    keyword,
  );

  // 100 / 10 = [1,2,3,4,5,6,7,8,9,10]
  const pageCount = Math.ceil(
    serverData.totalCount / serverData.pageRequestDTO.size,
  );

  // 페이지 번호 클릭 시 호출
  const handlePageClick = (event: { selected: number }) => {
    //setCurrentPage(event.selected);
    const next = new URLSearchParams(searchParams);
    next.set('page', String(event.selected));
    setSearchParams(next);
  };

  // 검색
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;

    // 검색 시 첫 페이지가 뜨도록
    const next = new URLSearchParams(searchParams);
    next.set('page', '0');

    if (name === 'keyword') next.set('keyword', value);
    if (name === 'genre') next.set('genre', value ? value : '0');

    setSearchParams(next);
  };

  //if (loading) return <Loading />;
  if (error) return <Error />;

  return (
    <BasicLayout>
      <header className="mb-6 flex">
        <h1 className="grow text-[32px]">Book List</h1>
        <div>
          <input
            onChange={handleChange}
            type="text"
            name="keyword"
            placeholder="Search by title or author"
            className="w-50 rounded-sm border-2 border-gray-300 p-2 text-[.9em] leading-tight outline-0"
          />
          <select
            onChange={handleChange}
            name="genre"
            className="ml-2 rounded-sm border-2 border-gray-300 p-2 text-[.9em] leading-tight outline-0"
          >
            <option value="">All Genres</option>
            {genres.map((genre, idx) => (
              <option key={idx} value={idx + 1}>
                {genre}
              </option>
            ))}
          </select>
        </div>
      </header>
      {loading ? (
        <Loading />
      ) : (
        <NovelList
          dtoList={serverData.dtoList}
          toggle={toggleAvailable}
          handlePageClick={handlePageClick}
          pageCount={pageCount}
          currentPage={currentPage}
        />
      )}
    </BasicLayout>
  );
};

export default Home;
