import {request} from "@@/plugin-request/request";

const BASE_URI = '/api/thirdApp';

export async function appPage(params?: Record<string, any>) {
  return request<R.Ret>(`${BASE_URI}/list`, {
    method: 'POST',
    data:params,
  });
}

export async function addApp(data?: Record<string, any>) {
  return request<R.Ret>(`${BASE_URI}`, {
    method: 'POST',
    data,
  });
}


export async function getApp(id: string) {
  return request<R.Ret>(`${BASE_URI}/${id}`, {
    method: 'GET',
  });
}

export async function delApp(id: string) {
  return request<R.Ret>(`${BASE_URI}/${id}`, {
    method: 'DELETE',
  });
}


