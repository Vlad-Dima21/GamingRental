'use server';

import { cookies } from 'next/headers';
import { jwtDecode } from 'jwt-decode';
import { revalidatePath } from 'next/cache';

type Session = { sub: string; exp: number; roles: string };
type FormState =
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
  const userEmail = formData.get('email') as string;
  const userPassword = formData.get('password') as string;

  const response = await fetch(`${process.env.BACKEND_URL}/api/auth/login`, {
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
      const {
        details,
        fieldName,
      }: { message: string; details: string; fieldName: string } =
        await response.json();
      return { error: { details, fieldName } };
    } catch (error) {
      return { error: { details: 'Invalid credentials' } };
    }
  }
};

export const logout = async (): Promise<void> => {
  cookies().set('session', '', { httpOnly: true, expires: new Date(0) });
  revalidatePath('/');
};

export const getSession = async (): Promise<Session | null> => {
  const session = cookies().get('session')?.value;
  if (!session) return null;
  return jwtDecode(session);
};
