'use client';

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
  readOnly?: boolean;
}

export default function GameCard({
  gameCopy,
  readOnly,
  ...props
}: GameCardProps) {
  const { cart, setUserCart } = useContext(CartContext);
  const isInCart = cart?.gameCopiesIds?.includes(gameCopy.gameCopyId);

  const removeFromCart = () => {
    if (!cart) return;
    const cartCopy = cart.gameCopiesIds?.slice() ?? [];
    setUserCart({
      ...cart,
      gameCopiesIds: cartCopy.filter((c) => c != gameCopy.gameCopyId),
    });
  };

  const addToCart = () => {
    if (!cart) return;
    const cartCopy = cart.gameCopiesIds?.slice() ?? [];
    setUserCart({
      ...cart,
      gameCopiesIds: [...cartCopy, gameCopy.gameCopyId],
    });
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
        <Badge variant='outline'>{gameCopy.gameBase.gameGenre}</Badge>
      </div>
      {!readOnly && !isInCart && (
        <Button onClick={addToCart} className='mt-6'>
          Add to cart
        </Button>
      )}
      {!readOnly && isInCart && (
        <Button variant='outline' onClick={removeFromCart} className='mt-6'>
          Remove from cart
        </Button>
      )}
    </Card>
  );
}
