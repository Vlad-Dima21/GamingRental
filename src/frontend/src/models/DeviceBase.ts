import Device from './Device';
import GameCopy from './GameCopy';

export default interface DeviceBase {
  deviceBaseId: number;
  deviceBaseName: string;
  deviceBaseProducer: string;
  deviceBaseYearOfRelease: number;
  noOfUnitsAvailable: number;
  deviceBaseImageUrl: string;

  deviceBaseUnits: Device[];
  deviceBaseGameCopies: GameCopy[];
}
