import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IVehicleBrand } from '../vehicle-brand.model';

@Component({
  standalone: true,
  selector: 'jhi-vehicle-brand-detail',
  templateUrl: './vehicle-brand-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class VehicleBrandDetailComponent {
  vehicleBrand = input<IVehicleBrand | null>(null);

  previousState(): void {
    window.history.back();
  }
}
