export interface IVehicleType {
  id: number;
  name?: string | null;
}

export type NewVehicleType = Omit<IVehicleType, 'id'> & { id: null };
