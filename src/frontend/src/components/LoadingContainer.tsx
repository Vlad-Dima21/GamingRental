import { cn } from '@/lib/utils';
import { Loader2 } from 'lucide-react';

export default function LoadingContainer({
  loading,
  className,
  children,
}: {
  loading: boolean;
  className?: string;
  children: React.ReactNode;
}) {
  return (
    <div className={cn('w-full', className)}>
      {loading ? <Loader2 /> : children}
    </div>
  );
}
