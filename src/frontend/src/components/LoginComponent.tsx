'use client';

import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { login } from '@/helpers/auth';
import { useFormState } from 'react-dom';

const LoginPage = () => {
  const [formState, formAction] = useFormState(login, undefined);
  return (
    <div className='p-16 max-w-5xl md:min-w-[600px] m-auto'>
      <Card>
        <CardHeader>
          <CardTitle>Sign In</CardTitle>
        </CardHeader>
        <form action={formAction}>
          <CardContent>
            <Label htmlFor='email'>Email</Label>
            <Input
              id='email'
              name='email'
              type='email'
              required
              defaultValue={'test@email.com'}
            />
            <Label htmlFor='password'>Password</Label>
            <Input
              id='password'
              name='password'
              type='password'
              required
              defaultValue={'test'}
            />
          </CardContent>
          <CardFooter>
            <Button>Sign In</Button>
            {!!formState?.success && (
              <span className='text-green-400'>{formState.success}</span>
            )}
            {!!formState?.error && (
              <span className='text-red-500'>{formState.error}</span>
            )}
          </CardFooter>
        </form>
      </Card>
    </div>
  );
};

export default LoginPage;
