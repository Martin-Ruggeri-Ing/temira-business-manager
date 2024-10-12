import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IVehicleType } from '../vehicle-type.model';

@Component({
  standalone: true,
  selector: 'jhi-vehicle-type-detail',
  templateUrl: './vehicle-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class VehicleTypeDetailComponent {
  vehicleType = input<IVehicleType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
