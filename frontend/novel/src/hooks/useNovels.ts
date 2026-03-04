// 전체 목록 가져오기 hook

import { useCallback, useEffect, useState } from 'react';
import { getList, putAvailable } from '../apis/novelApis';
import { initialPageState, type Novel, type PageResult } from '../types/book';

export const useNovels = (
  page: number,
  size: number,
  genre: number,
  keyword: string,
) => {
  const [serverData, setServerData] =
    useState<PageResult<Novel>>(initialPageState);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<unknown>(null);

  // 전체 목록
  const fetchData = useCallback(async () => {
    // react-paginate 가 페이지 클릭시 자동으로 -1 처리
    page = page + 1;

    try {
      setLoading(true);
      const data = await getList({ page, size, genre, keyword });
      setServerData(data);
    } catch (error) {
      console.error(error);
      setError(error);
    } finally {
      setLoading(false);
    }
  }, [page, size, keyword, genre]);

  // available 수정
  const toggleAvailable = useCallback(
    async (id: number, available: boolean) => {
      // async ({id,available}:NovelPut) => {
      const result = await putAvailable({
        id: id,
        available: !available,
      });

      console.log('toggleAvailable ', result);
      fetchData();
    },
    [fetchData],
  );

  useEffect(() => {
    // 렌더링 시 함수호출
    fetchData();
  }, [fetchData]);

  return { serverData, loading, error, toggleAvailable };
};
