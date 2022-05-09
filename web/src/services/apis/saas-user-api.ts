import {request} from "@@/plugin-request/request";

const BASE_URI = '/api/user';

export async function userPage(params?: Record<string, any>) {
  return request<R.Ret>(`${BASE_URI}/list`, {
    method: 'POST',
    data:params,
  });
}
