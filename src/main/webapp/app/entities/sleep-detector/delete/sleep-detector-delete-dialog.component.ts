import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISleepDetector } from '../sleep-detector.model';
import { SleepDetectorService } from '../service/sleep-detector.service';

@Component({
  standalone: true,
  templateUrl: './sleep-detector-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SleepDetectorDeleteDialogComponent {
  sleepDetector?: ISleepDetector;

  protected sleepDetectorService = inject(SleepDetectorService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sleepDetectorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
