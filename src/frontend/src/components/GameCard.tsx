import { CartContext } from '@/contexts/cart-context';
import DeviceBase from '@/models/DeviceBase';
import GameCopy from '@/models/GameCopy';
import { HTMLAttributes, useContext } from 'react';
import { Card } from './ui/card';
import { Gamepad } from 'lucide-react';
import { Button } from './ui/button';
import { Badge } from './ui/badge';
import { cn } from '@/lib/utils';

interface GameCardProps extends HTMLAttributes<HTMLDivElement> {
  gameCopy: GameCopy;
  device: DeviceBase;
}

export default function GameCard({
  gameCopy,
  device,
  ...props
}: GameCardProps) {
  const { cart, setUserCart } = useContext(CartContext);
  const isInCart = cart.some(
    (item) =>
      item.deviceBaseId == device.deviceBaseId &&
      item.gameCopies?.includes(gameCopy.gameCopyId)
  );
  const removeFromCart = () => {
    const cartCopy = cart.slice();
    const cartBaseIndex = cartCopy.findIndex(
        (item) => item.deviceBaseId === device.deviceBaseId
      ),
      cartDevice = cartCopy[cartBaseIndex];
    cartDevice.gameCopies = cartDevice.gameCopies?.filter(
      (gc) => gc != gameCopy.gameCopyId
    );
    !cartDevice.deviceUnits?.length && !cartDevice.gameCopies?.length
      ? setUserCart(cartCopy.toSpliced(cartBaseIndex, 1))
      : setUserCart(cartCopy);
  };

  const addToCart = () => {
    const cartCopy = cart.slice();
    let cartDevice = cartCopy.find(
      (c) => c.deviceBaseId === device.deviceBaseId
    );
    if (!cartDevice) {
      cartDevice = { deviceBaseId: device.deviceBaseId };
      cartCopy.push(cartDevice);
    }
    cartDevice.gameCopies = [
      ...(cartDevice.gameCopies ?? []),
      gameCopy.gameCopyId,
    ];
    setUserCart(cartCopy);
  };

  return (
    <Card
      {...props}
      className={cn(
        'flex flex-col gap-2 p-5 justify-around bg-white/70 backdrop-blur-sm hover:shadow-md',
        props.className
      )}
    >
      <div className='flex items-center gap-2'>
        <span>{gameCopy.gameBase.gameName}</span>
        <Badge variant='default'>{gameCopy.gameBase.gameGenre}</Badge>
      </div>
      {!isInCart && (
        <Button onClick={addToCart} className='mt-6'>
          Add to cart
        </Button>
      )}
      {isInCart && (
        <Button variant='outline' onClick={removeFromCart} className='mt-6'>
          Remove from cart
        </Button>
      )}
    </Card>
  );
}
