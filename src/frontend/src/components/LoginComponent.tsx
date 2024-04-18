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

const LoginComponent = () => {
  const [formState, formAction] = useFormState(login, undefined);
  return (
    <div className='p-16 max-w-5xl md:min-w-[700px] m-auto'>
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
              className={
                formState?.error?.fieldName === 'userEmail'
                  ? 'border-red-600 !ring-red-600'
                  : ''
              }
            />
            <Label htmlFor='password'>Password</Label>
            <Input
              id='password'
              name='password'
              type='password'
              required
              defaultValue={'test'}
              className={
                formState?.error?.fieldName === 'userPassword'
                  ? 'border-red-600 !ring-red-600'
                  : ''
              }
            />
          </CardContent>
          <CardFooter className='flex justify-between gap-2'>
            <Button>Sign In</Button>
            {!!formState?.success && (
              <span className='text-green-400'>{formState.success}</span>
            )}
            {!!formState?.error && (
              <span className='text-red-500'>{formState.error.details}</span>
            )}
          </CardFooter>
        </form>
      </Card>
    </div>
  );
};

export default LoginComponent;
