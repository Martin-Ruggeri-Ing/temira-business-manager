import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVehicleBrand } from '../vehicle-brand.model';
import { VehicleBrandService } from '../service/vehicle-brand.service';

@Component({
  standalone: true,
  templateUrl: './vehicle-brand-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VehicleBrandDeleteDialogComponent {
  vehicleBrand?: IVehicleBrand;

  protected vehicleBrandService = inject(VehicleBrandService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehicleBrandService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
