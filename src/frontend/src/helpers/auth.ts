'use server';

import { cookies } from 'next/headers';
import { jwtDecode } from 'jwt-decode';
import { revalidatePath } from 'next/cache';
import { redirect } from 'next/navigation';

export type Session = {
  sub: string;
  exp: number;
  roles: string;
  token: string;
};
export type FormState =
  | {
      error?: {
        details: string;
        fieldName?: string;
      };
      success?: string;
    }
  | undefined;

export const login = async (
  prevState: FormState,
  formData: FormData
): Promise<FormState> => {
  const userEmail = formData.get('userEmail') as string;
  const userPassword = formData.get('userPassword') as string;

  const response = await fetch(`${process.env.BACKEND_URL}/auth/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ userEmail, userPassword }),
  });

  if (response.ok) {
    const { token }: { token: string; userEmail: string } =
      await response.json();
    const decodedToken: Session = jwtDecode(token);
    if (decodedToken.exp) {
      cookies().set('session', token, {
        httpOnly: true,
        expires: new Date(decodedToken.exp * 1000),
      });
      return { success: 'Login successful' };
    }
  } else {
    try {
      const error: { message: string; details: string; fieldName: string } =
        await response.json();
      const { details, message, fieldName } = error;
      return {
        error: {
          details: details ?? message ?? 'Something went wrong',
          fieldName,
        },
      };
    } catch (error) {
      return { error: { details: 'Invalid credentials' } };
    }
  }
};

export const register = async (
  prevState: FormState,
  formData: FormData
): Promise<FormState> => {
  const userEmail = formData.get('userEmail') as string;
  const userPassword = formData.get('userPassword') as string;
  const userName = formData.get('userName') as string;
  const userPhone = formData.get('userPhone') as string;

  const response = await fetch(
    `${process.env.BACKEND_URL}/auth/register/client`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ userEmail, userPassword, userName, userPhone }),
    }
  );

  if (response.ok) {
    const { token }: { token: string } = await response.json();
    const decodedToken: Session = jwtDecode(token);
    if (decodedToken.exp) {
      cookies().set('session', token, {
        httpOnly: true,
        expires: new Date(decodedToken.exp * 1000),
      });
      return { success: 'Registration successful' };
    }
  } else {
    try {
      const {
        details,
        fieldName,
      }: { message: string; details: string; fieldName: string } =
        await response.json();
      return {
        error: { details: details ?? 'Something went wrong', fieldName },
      };
    } catch (error) {
      return { error: { details: 'Invalid info' } };
    }
  }
};

export const logout = async (): Promise<void> => {
  cookies().set('session', '', { httpOnly: true, expires: new Date(0) });
  revalidatePath('/');
};

export const getSession = async (): Promise<Session | null> => {
  const session = cookies().get('session')?.value;
  if (!session) {
    return null;
  }
  return { ...(jwtDecode(session) as Session), token: session };
};
