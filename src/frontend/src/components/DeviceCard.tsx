'use client';

import { HTMLAttributes, useContext } from 'react';
import { Card } from './ui/card';
import Device from '@/models/Device';
import { Badge } from './ui/badge';
import { Gamepad } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { CartContext } from '@/contexts/cart-context';

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
  const isInCart = cart.some((item) => item.deviceId === device.deviceId);

  const removeFromCart = () => {
    const cartIndex = cart.findIndex(
      (item) => item.deviceId === device.deviceId
    );
    if (cartIndex === -1) return;
    setUserCart(cart.toSpliced(cartIndex, 1));
  };

  return (
    <Card
      {...props}
      className='flex flex-col gap-2 p-5 justify-around bg-white/70 backdrop-blur-sm hover:shadow-md'
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
          onClick={() => setUserCart([...cart, { deviceId: device.deviceId }])}
          disabled={!device.deviceIsAvailable}
          className='mt-6'
        >
          Add to cart
        </Button>
      )}
      {isInCart && (
        <Button
          variant={'outline'}
          onClick={removeFromCart}
          disabled={!device.deviceIsAvailable}
          className='mt-6'
        >
          Remove from cart
        </Button>
      )}
    </Card>
  );
}
