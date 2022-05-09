import {request} from "@@/plugin-request/request";

const BASE_URI = '/api/spiRecord';

export async function spiRecordPage(params?: Record<string, any>) {
  return request<R.Ret>(`${BASE_URI}/list`, {
    method: 'POST',
    data: params,
  });
}
