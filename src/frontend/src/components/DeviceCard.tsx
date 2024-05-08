'use client';

import { HTMLAttributes, useContext } from 'react';
import { Card } from './ui/card';
import Device from '@/models/Device';
import { Badge } from './ui/badge';
import { Gamepad } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { CartContext } from '@/contexts/cart-context';
import { cn } from '@/lib/utils';

interface DeviceCardProps extends HTMLAttributes<HTMLDivElement> {
  device: Device;
  index: number;
}

export default function DeviceCard({
  device,
  index,
  ...props
}: DeviceCardProps) {
  const { cart, setUserCart } = useContext(CartContext);
  const isInCart = cart?.deviceUnitId == device.deviceId;

  const removeFromCart = () => {
    setUserCart(void 0);
  };

  const addToCart = () => {
    setUserCart({
      deviceBaseId: device.deviceBaseId,
      deviceUnitId: device.deviceId,
      numberOfDays: 30,
    });
  };

  return (
    <Card
      {...props}
      className={cn(
        'flex flex-col gap-2 p-5 justify-around bg-white/70 backdrop-blur-sm hover:shadow-md min-w-[200px] transition-all duration-200',
        props.className
      )}
    >
      <div className='flex items-center gap-2'>
        <span>Unit no. {index + 1}</span>
        <Badge variant={device.deviceIsAvailable ? 'default' : 'destructive'}>
          {device.deviceIsAvailable ? 'Available' : 'Not available'}
        </Badge>
      </div>
      <div className='flex items-center gap-2'>
        <Gamepad />
        <span>
          {device.deviceNumberOfControllers}{' '}
          {device.deviceNumberOfControllers > 1 ? 'controllers' : 'controller'}
        </span>
      </div>
      {!isInCart && (
        <Button
          onClick={addToCart}
          disabled={!device.deviceIsAvailable}
          className='mt-6'
        >
          {!cart ? 'Add to cart' : 'Replace in cart'}
        </Button>
      )}
      {isInCart && (
        <Button variant={'outline'} onClick={removeFromCart} className='mt-6'>
          Remove from cart
        </Button>
      )}
    </Card>
  );
}
