import LoginComponent from '@/components/LoginComponent';
import { getSession } from '@/helpers/auth';
import { Metadata } from 'next';
import { redirect } from 'next/navigation';

export const metadata: Metadata = {
  title: 'Login',
};

export default async function Login() {
  const session = await getSession();
  if (session !== null) {
    redirect('/');
  }
  return <LoginComponent />;
}
