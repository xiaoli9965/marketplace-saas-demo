declare namespace R {
  type Ret = {
    code: number;
    message: string;
  };

  type DataRet<T> = {
    code: number;
    message: string;
    data?: T[];
  };

  type PageRet = {
    code?: number;
    message?: string;
    data?: any[];
    totalPage?: string;
    size?: string;
    page?: string;
    total?: string;
  };
}
