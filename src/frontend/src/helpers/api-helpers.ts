'use server';

import { ServerResponse } from './server-response';
import { getSession } from './auth';

const apiUrl = process.env.BACKEND_URL;

async function commonFetch(
  url: string,
  method: string,
  data?: object
): Promise<Response> {
  const session = await getSession();
  return await fetch(`${apiUrl}${url}`, {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...(session && { Authorization: `Bearer ${session.token}` }),
    },
    ...(data && { body: JSON.stringify(data) }),
  });
}

export async function get(url: string, data?: object): Promise<Response> {
  return await commonFetch(url, 'GET', data);
}

export async function clientGet(url: string, data?: object): Promise<string> {
  const resp = await get(url, data),
    json = await resp.json();
  return JSON.stringify(new ServerResponse(resp.ok, resp.status, json));
}

export async function post(url: string, data?: object): Promise<Response> {
  return await commonFetch(url, 'POST', data);
}

export async function clientPost(url: string, data?: object): Promise<string> {
  const resp = await post(url, data),
    json = await resp.json();
  return JSON.stringify(new ServerResponse(resp.ok, resp.status, json));
}

export async function patch(url: string, data?: object): Promise<Response> {
  return await commonFetch(url, 'PATCH', data);
}

export async function clientPatch(url: string, data?: object): Promise<string> {
  const resp = await patch(url, data),
    json = await resp.json();
  return JSON.stringify(new ServerResponse(resp.ok, resp.status, json));
}
