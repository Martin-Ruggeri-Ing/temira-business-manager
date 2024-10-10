import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { IDriver } from 'app/entities/driver/driver.model';
import { IUser } from 'app/entities/user/user.model';

export interface ISleepDetector {
  id: number;
  vehicle?: Pick<IVehicle, 'id'> | null;
  driver?: Pick<IDriver, 'id' | 'firstName'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewSleepDetector = Omit<ISleepDetector, 'id'> & { id: null };
