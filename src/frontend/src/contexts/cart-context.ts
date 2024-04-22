import { createContext, useState } from 'react';

export interface CartItem {
  deviceUnitId: number;
  deviceBaseId: number;
  gameCopiesIds?: number[];
  numberOfDays: number;
}

export interface UserContext {
  email: string | undefined;
  cart: CartItem | undefined;
  setUserCart: (cart: CartItem | undefined) => void;
}

export const CartContext = createContext<UserContext>({
  email: '',
  cart: void 0,
  setUserCart: () => {},
});

const CART_KEY = 'cart';

export function getCartFromLocalStorage(email: string): CartItem | undefined {
  const stringifiedCart = localStorage?.getItem(CART_KEY);
  if (stringifiedCart) {
    try {
      const entireCart: { [email: string]: CartItem } =
        JSON.parse(stringifiedCart);
      if (entireCart?.[email]) {
        return entireCart[email];
      }
    } catch (e: any) {
      console.error(
        `Failed to parse cart from local storage for user ${email}: ${e.message}`
      );
    }
  }
}

export function setCartToLocalStorage(
  email: string,
  cart: CartItem | undefined
) {
  const stringifiedCart = localStorage?.getItem(CART_KEY);
  try {
    const entireCart: { [email: string]: CartItem | undefined } =
      stringifiedCart ? JSON.parse(stringifiedCart) : {};
    entireCart[email] = cart;
    localStorage.setItem(CART_KEY, JSON.stringify(entireCart));
  } catch (e: any) {
    console.error(
      `Failed to save cart to local storage for user ${email}: ${e.message}`
    );
  }
}
