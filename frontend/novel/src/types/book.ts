export const initialNovel = {
  id: 0,
  title: '',
  author: '',
  publishedDate: '',
  available: false,
  summary: '',
  description: '',
  gid: 0,
  genreName: '',
  rating: 0,
  email: '',
};

export type Novel = {
  id: number;
  title: string;
  author: string;
  publishedDate: string;
  available: boolean;
  summary: string;
  description: string;
  gid: number;
  genreName: string;
  rating: number;
  email: string;
};

export type NovelPost = Omit<Novel, 'id' | 'description'>;

// 페이지나누기
// 서버 : PageResultDTO
export type PageRequestDTO = {
  page: number;
  size: number;
  genre?: number;
  keyword?: string;
};

export type PageResult<T> = {
  dtoList: T[];
  pageNumList: number[];
  pageRequestDTO: PageRequestDTO;
  prev: boolean;
  next: boolean;
  prevPage: number;
  nextPage: number;
  totalPage: number;
  current: number;
  totalCount: number;
};

export const initialPageState: PageResult<Novel> = {
  dtoList: [],
  pageNumList: [],
  pageRequestDTO: { page: 0, size: 10, keyword: '', genre: 0 },
  prev: false,
  next: false,
  prevPage: 0,
  nextPage: 0,
  totalPage: 0,
  current: 0,
  totalCount: 0,
};

export type NovelPut = {
  id: number;
  available: boolean;
};

// add, update 시 사용할 타입
export type NovelUpSert = {
  novel: Novel;
  onSubmit: (formData: Novel) => void;
  onCancel: (id: number) => void;
};

export type ListProps = {
  dtoList: Novel[];
  toggle: (id: number, available: boolean) => void;
  handlePageClick: (event: { selected: number }) => void;
  pageCount: number;
  currentPage: number;
};
