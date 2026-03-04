// id : string 으로 처리하는 이유는
// 주소줄에서 id 를 받아올때는 string 형태임
// spring server 단에서 형변환 해서 받을 수 있기 때문에 상관없음

import { useCallback, useEffect, useState } from 'react';
import { initialNovel, type Novel } from '../types/book';
import { getRow } from '../apis/novelApis';

export const useNovel = (id?: string) => {
  const [serverData, setServerData] = useState<Novel>(initialNovel);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<unknown>(null);

  const fetchData = useCallback(async () => {
    if (!id) return;

    try {
      setLoading(true);
      const data = await getRow(id);
      setServerData(data);
    } catch (error) {
      console.error(error);
      setError(error);
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    // 렌더링 시 함수호출
    fetchData();
  }, [fetchData]);

  return { serverData, loading, error };
};
