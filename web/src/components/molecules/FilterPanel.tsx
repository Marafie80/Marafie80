'use client';

import React, { useState } from 'react';
import { FilterGroup, SearchFilters } from '@/types';
import { Button } from '@/components/atoms';

interface FilterPanelProps {
  filters: FilterGroup[];
  activeFilters: SearchFilters;
  onChange: (filters: SearchFilters) => void;
  onReset: () => void;
  className?: string;
}

const FilterPanel: React.FC<FilterPanelProps> = ({
  filters,
  activeFilters,
  onChange,
  onReset,
  className = '',
}) => {
  const [expandedSections, setExpandedSections] = useState<Set<string>>(
    new Set(filters.map((f) => f.id))
  );

  const toggleSection = (sectionId: string) => {
    const newExpanded = new Set(expandedSections);
    if (newExpanded.has(sectionId)) {
      newExpanded.delete(sectionId);
    } else {
      newExpanded.add(sectionId);
    }
    setExpandedSections(newExpanded);
  };

  const handleCheckboxChange = (filterId: string, value: string) => {
    const currentValues = (activeFilters as any)[filterId] || [];
    const newValues = currentValues.includes(value)
      ? currentValues.filter((v: string) => v !== value)
      : [...currentValues, value];

    onChange({
      ...activeFilters,
      [filterId]: newValues.length > 0 ? newValues : undefined,
    });
  };

  const handleRangeChange = (filterId: string, min: number, max: number) => {
    onChange({
      ...activeFilters,
      [`${filterId}Min`]: min,
      [`${filterId}Max`]: max,
    });
  };

  return (
    <div className={`bg-neutral-white rounded-lg border border-neutral-light-gray shadow-sm ${className}`}>
      <div className="p-6 border-b border-neutral-light-gray flex items-center justify-between">
        <h3 className="text-h3 font-semibold">Filters</h3>
        <button
          onClick={onReset}
          className="text-body-sm text-primary-gold hover:underline"
        >
          Clear All
        </button>
      </div>

      <div className="divide-y divide-neutral-light-gray">
        {filters.map((filterGroup) => (
          <div key={filterGroup.id} className="p-4">
            <button
              onClick={() => toggleSection(filterGroup.id)}
              className="w-full flex items-center justify-between text-left group"
            >
              <span className="text-body font-semibold text-primary-dark-blue">
                {filterGroup.label}
              </span>
              <svg
                className={`w-5 h-5 text-neutral-medium-gray transition-transform ${
                  expandedSections.has(filterGroup.id) ? 'rotate-180' : ''
                }`}
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M19 9l-7 7-7-7"
                />
              </svg>
            </button>

            {expandedSections.has(filterGroup.id) && (
              <div className="mt-4 space-y-2">
                {filterGroup.type === 'checkbox' && filterGroup.options && (
                  <div className="space-y-2 max-h-64 overflow-y-auto">
                    {filterGroup.options.map((option) => {
                      const isChecked = (
                        (activeFilters as any)[filterGroup.id] || []
                      ).includes(option.value);

                      return (
                        <label
                          key={option.id}
                          className="flex items-center gap-2 cursor-pointer group"
                        >
                          <input
                            type="checkbox"
                            checked={isChecked}
                            onChange={() =>
                              handleCheckboxChange(filterGroup.id, option.value as string)
                            }
                            className="w-4 h-4 rounded border-neutral-medium-gray text-primary-gold
                                     focus:ring-2 focus:ring-primary-gold/20 cursor-pointer"
                          />
                          <span className="text-body text-primary-dark-blue group-hover:text-primary-gold transition-colors">
                            {option.label}
                            {option.count !== undefined && (
                              <span className="text-neutral-medium-gray ml-1">
                                ({option.count})
                              </span>
                            )}
                          </span>
                        </label>
                      );
                    })}
                  </div>
                )}

                {filterGroup.type === 'range' && (
                  <div className="space-y-4">
                    <div className="px-2">
                      <input
                        type="range"
                        min={filterGroup.min || 0}
                        max={filterGroup.max || 100}
                        value={(activeFilters as any)[`${filterGroup.id}Max`] || filterGroup.max}
                        onChange={(e) =>
                          handleRangeChange(
                            filterGroup.id,
                            (activeFilters as any)[`${filterGroup.id}Min`] || filterGroup.min || 0,
                            parseInt(e.target.value)
                          )
                        }
                        className="w-full h-2 bg-neutral-light-gray rounded-lg appearance-none cursor-pointer
                                 accent-primary-gold"
                      />
                    </div>

                    <div className="flex items-center gap-4">
                      <div className="flex-1">
                        <label className="block text-caption text-neutral-medium-gray mb-1">
                          Min
                        </label>
                        <input
                          type="number"
                          value={(activeFilters as any)[`${filterGroup.id}Min`] || filterGroup.min || 0}
                          onChange={(e) =>
                            handleRangeChange(
                              filterGroup.id,
                              parseInt(e.target.value),
                              (activeFilters as any)[`${filterGroup.id}Max`] || filterGroup.max || 100
                            )
                          }
                          className="w-full px-3 py-2 border border-neutral-light-gray rounded text-body-sm"
                        />
                      </div>

                      <div className="flex-1">
                        <label className="block text-caption text-neutral-medium-gray mb-1">
                          Max
                        </label>
                        <input
                          type="number"
                          value={(activeFilters as any)[`${filterGroup.id}Max`] || filterGroup.max || 100}
                          onChange={(e) =>
                            handleRangeChange(
                              filterGroup.id,
                              (activeFilters as any)[`${filterGroup.id}Min`] || filterGroup.min || 0,
                              parseInt(e.target.value)
                            )
                          }
                          className="w-full px-3 py-2 border border-neutral-light-gray rounded text-body-sm"
                        />
                      </div>
                    </div>
                  </div>
                )}

                {filterGroup.type === 'radio' && filterGroup.options && (
                  <div className="space-y-2">
                    {filterGroup.options.map((option) => {
                      const isChecked =
                        (activeFilters as any)[filterGroup.id] === option.value;

                      return (
                        <label
                          key={option.id}
                          className="flex items-center gap-2 cursor-pointer group"
                        >
                          <input
                            type="radio"
                            name={filterGroup.id}
                            checked={isChecked}
                            onChange={() =>
                              onChange({
                                ...activeFilters,
                                [filterGroup.id]: option.value,
                              })
                            }
                            className="w-4 h-4 border-neutral-medium-gray text-primary-gold
                                     focus:ring-2 focus:ring-primary-gold/20 cursor-pointer"
                          />
                          <span className="text-body text-primary-dark-blue group-hover:text-primary-gold transition-colors">
                            {option.label}
                          </span>
                        </label>
                      );
                    })}
                  </div>
                )}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default FilterPanel;
