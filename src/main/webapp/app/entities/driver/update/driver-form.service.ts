import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDriver, NewDriver } from '../driver.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDriver for edit and NewDriverFormGroupInput for create.
 */
type DriverFormGroupInput = IDriver | PartialWithRequiredKeyOf<NewDriver>;

type DriverFormDefaults = Pick<NewDriver, 'id'>;

type DriverFormGroupContent = {
  id: FormControl<IDriver['id'] | NewDriver['id']>;
  firstName: FormControl<IDriver['firstName']>;
  lastName: FormControl<IDriver['lastName']>;
  user: FormControl<IDriver['user']>;
};

export type DriverFormGroup = FormGroup<DriverFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DriverFormService {
  createDriverFormGroup(driver: DriverFormGroupInput = { id: null }): DriverFormGroup {
    const driverRawValue = {
      ...this.getFormDefaults(),
      ...driver,
    };
    return new FormGroup<DriverFormGroupContent>({
      id: new FormControl(
        { value: driverRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(driverRawValue.firstName, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(50)],
      }),
      lastName: new FormControl(driverRawValue.lastName, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(50)],
      }),
      user: new FormControl(driverRawValue.user),
    });
  }

  getDriver(form: DriverFormGroup): IDriver | NewDriver {
    return form.getRawValue() as IDriver | NewDriver;
  }

  resetForm(form: DriverFormGroup, driver: DriverFormGroupInput): void {
    const driverRawValue = { ...this.getFormDefaults(), ...driver };
    form.reset(
      {
        ...driverRawValue,
        id: { value: driverRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DriverFormDefaults {
    return {
      id: null,
    };
  }
}
