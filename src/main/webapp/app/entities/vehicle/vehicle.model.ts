import { IUser } from 'app/entities/user/user.model';
import { IVehicleType } from 'app/entities/vehicle-type/vehicle-type.model';
import { IVehicleBrand } from 'app/entities/vehicle-brand/vehicle-brand.model';

export interface IVehicle {
  id: number;
  model?: number | null;
  name?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  type?: Pick<IVehicleType, 'id' | 'name'> | null;
  brand?: Pick<IVehicleBrand, 'id' | 'name'> | null;
}

export type NewVehicle = Omit<IVehicle, 'id'> & { id: null };
