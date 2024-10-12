import { IUser } from 'app/entities/user/user.model';

export interface ISleepDetector {
  id: number;
  name?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewSleepDetector = Omit<ISleepDetector, 'id'> & { id: null };
