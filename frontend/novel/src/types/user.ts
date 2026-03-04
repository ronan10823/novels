export type User = {
  email: string;
  pw: string;
  nickname: string;
  social: boolean;
};

export type LoginResponse = {
  email: string;
  pw?: string;
  nickname: string;
  social: boolean;
  roles: string[];
  accessToken: string;
};

export type LoginForm = {
  email: string;
  pw: string;
};
