import { TestBed } from '@angular/core/testing';

import { QuoteClientService } from './quote-client.service';

describe('QuoteClientService', () => {
  let service: QuoteClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuoteClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
